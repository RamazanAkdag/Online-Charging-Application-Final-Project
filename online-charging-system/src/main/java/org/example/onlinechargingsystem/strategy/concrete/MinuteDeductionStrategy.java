package org.example.onlinechargingsystem.strategy.concrete;

import com.ramobeko.ignite.IgniteSubscriber;
import com.ramobeko.ignite.IgniteBalance;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.onlinechargingsystem.repository.ignite.IgniteSubscriberRepository;
import org.example.onlinechargingsystem.strategy.abstrct.IUsageDeductionStrategy;

public class MinuteDeductionStrategy implements IUsageDeductionStrategy {
    private static final Logger logger = LogManager.getLogger(MinuteDeductionStrategy.class);

    @Override
    public void deductBalance(IgniteSubscriber subscriber, int amount, IgniteSubscriberRepository igniteSubscriberRepository, Long subscNumber) {
        IgniteBalance balance = subscriber.getBalances().get(0);

        if (balance.getLevelMinutes() < amount) {
            logger.error("❌ Insufficient minutes balance for subscNumber: {}", subscNumber);
            throw new RuntimeException("Insufficient minutes balance");
        }

        balance.setLevelMinutes(balance.getLevelMinutes() - amount);
        igniteSubscriberRepository.save(subscNumber, subscriber);
        logger.info("✅ Updated minutes balance for subscNumber: {}", subscNumber);
    }
}
