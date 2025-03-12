package org.example.onlinechargingsystem.service.concrete;

import com.ramobeko.dgwtgf.model.UsageType;
import com.ramobeko.ignite.IgniteSubscriber;
import com.ramobeko.ignite.IgniteBalance;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.onlinechargingsystem.repository.ignite.IgniteSubscriberRepository;
import org.example.onlinechargingsystem.service.abstrct.IBalanceService;
import org.example.onlinechargingsystem.strategy.abstrct.IUsageDeductionStrategy;
import org.example.onlinechargingsystem.strategy.concrete.deduction.UsageDeductionStrategyFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BalanceService implements IBalanceService {

    private static final Logger logger = LogManager.getLogger(BalanceService.class);

    private final IgniteSubscriberRepository igniteSubscriberRepository;

    @Override
    public IgniteBalance getBalance(Long subscNumber) {
        logger.info("🔍 Fetching balance for subscriber: {}", subscNumber);

        IgniteSubscriber subscriber = igniteSubscriberRepository.findById(subscNumber)
                .orElseThrow(() -> {
                    logger.error("❌ Subscriber not found in Ignite for subscNumber: {}", subscNumber);
                    return new RuntimeException("Subscriber not found");
                });

        logger.info("📜 Subscriber details: {}", subscriber);

        if (subscriber.getBalances().isEmpty()) {
            logger.error("❌ No balance record found for subscriber: {}", subscNumber);
            throw new RuntimeException("Balance not found");
        }

        return subscriber.getBalances().get(0);
    }

    @Override
    public void deductBalance(Long subscNumber, int amount, UsageType usageType) {
        logger.info("📉 Deducting {} {} from balance for subscNumber: {}", amount, usageType, subscNumber);

        IgniteSubscriber subscriber = igniteSubscriberRepository.findById(subscNumber)
                .orElseThrow(() -> {
                    logger.error("❌ Subscriber not found: {}", subscNumber);
                    return new RuntimeException("Subscriber not found");
                });

        logger.info("📜 Subscriber details before deduction: {}", subscriber);

        // Kullanım Stratejisini Seç
        IUsageDeductionStrategy strategy = UsageDeductionStrategyFactory.getStrategy(usageType);
        strategy.deductBalance(subscriber, amount, igniteSubscriberRepository, subscNumber);

        logger.info("✅ Balance updated for subscriber: {}, UsageType: {}, Amount Deducted: {}", subscNumber, usageType, amount);
    }
}
