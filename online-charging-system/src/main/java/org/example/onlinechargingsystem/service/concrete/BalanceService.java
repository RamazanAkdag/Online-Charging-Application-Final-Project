package org.example.onlinechargingsystem.service.concrete;

import com.ramobeko.dgwtgf.model.UsageType;
import com.ramobeko.ignite.IgniteSubscriber;
import com.ramobeko.ignite.IgniteBalance;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.onlinechargingsystem.repository.ignite.IgniteSubscriberRepository;
import org.example.onlinechargingsystem.service.abstrct.IBalanceService;
import org.example.onlinechargingsystem.strategy.balancestrategy.abstrct.IUsageBalanceChecker;
import org.example.onlinechargingsystem.strategy.balancestrategy.abstrct.IUsageDeductionStrategy;
import org.example.onlinechargingsystem.strategy.balancestrategy.concrete.checker.UsageBalanceCheckerFactory;
import org.example.onlinechargingsystem.strategy.balancestrategy.concrete.deduction.UsageDeductionStrategyFactory;
import org.example.onlinechargingsystem.strategy.thresholdstrategy.concrete.UsageThresholdPolicy;
import org.springframework.stereotype.Service;
import com.ramobeko.kafka.message.NFKafkaMessage;


@Service
@RequiredArgsConstructor
public class BalanceService implements IBalanceService {

    private static final Logger logger = LogManager.getLogger(BalanceService.class);

    private final IgniteSubscriberRepository igniteSubscriberRepository;

    private final UsageThresholdPolicy thresholdPolicy;

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

    @Override
    public String evaluateUsageThreshold(Long subscNumber, double usageAmount, UsageType usageType) {
        logger.info("🔎 Evaluating usage threshold for subscriber: {}, UsageAmount: {}, UsageType: {}",
                subscNumber, usageAmount, usageType);

        IgniteBalance balance = getBalance(subscNumber);
        IUsageBalanceChecker balanceChecker = UsageBalanceCheckerFactory.getChecker(usageType);
        double initialBalance = balanceChecker.getAvailableBalance(balance);

        if (initialBalance <= 0) {
            logger.warn("⚠️ Subscriber {} has zero or negative balance. Skipping threshold evaluation.", subscNumber);
            return null;
        }

        double usagePercentage = (usageAmount / initialBalance) * 100;
        logger.info("🔢 Usage Percentage: {}%", String.format("%.2f", usagePercentage));


        return thresholdPolicy
                .evaluate(subscNumber, usageType, usagePercentage)
                .map(NFKafkaMessage::getNotificationType) // Burada mesaj başlığı alınıyor
                .orElse(null);
    }


    @Override
    public boolean hasSufficientBalance(Long subscNumber, double usageAmount, UsageType usageType) {
        logger.info("🔎 Checking if subscriber {} has sufficient balance for {} usage of amount {}", subscNumber, usageType, usageAmount);

        IgniteBalance balance = getBalance(subscNumber);
        IUsageBalanceChecker balanceChecker = UsageBalanceCheckerFactory.getChecker(usageType);
        double availableBalance = balanceChecker.getAvailableBalance(balance);

        logger.info("💰 Available balance: {}, Required usage amount: {}", availableBalance, usageAmount);

        return availableBalance >= usageAmount;
    }


}
