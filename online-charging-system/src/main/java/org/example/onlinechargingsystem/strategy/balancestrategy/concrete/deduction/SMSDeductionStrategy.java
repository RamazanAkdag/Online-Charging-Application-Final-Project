package org.example.onlinechargingsystem.strategy.balancestrategy.concrete.deduction;

import com.ramobeko.ignite.IgniteSubscriber;
import com.ramobeko.ignite.IgniteBalance;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.onlinechargingsystem.repository.ignite.IgniteSubscriberRepository;
import org.example.onlinechargingsystem.strategy.balancestrategy.abstrct.IUsageDeductionStrategy;

public class SMSDeductionStrategy implements IUsageDeductionStrategy {
    private static final Logger logger = LogManager.getLogger(SMSDeductionStrategy.class);

    @Override
    public void deductBalance(IgniteSubscriber subscriber, int amount, IgniteSubscriberRepository igniteSubscriberRepository, Long subscNumber) {
        IgniteBalance balance = subscriber.getBalances().get(0);

        if (balance.getLevelSms() < amount) {
            logger.error("❌ Insufficient SMS balance for subscNumber: {}", subscNumber);
            throw new RuntimeException("Insufficient SMS balance");
        }

        balance.setLevelSms(balance.getLevelSms() - amount);
        igniteSubscriberRepository.save(subscNumber, subscriber);
        logger.info("✅ Updated SMS balance for subscNumber: {}", subscNumber);
    }
}
