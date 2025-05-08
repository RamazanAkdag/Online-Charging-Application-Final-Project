package org.example.onlinechargingsystem.strategy.thresholdstrategy.concrete;

import com.ramobeko.dgwtgf.model.UsageType;
import com.ramobeko.kafka.message.NFKafkaMessage;
import org.example.onlinechargingsystem.strategy.thresholdstrategy.abstrct.UsageThresholdRule;

import java.time.Instant;

public class EightyPercentRule implements UsageThresholdRule {

    @Override
    public boolean isApplicable(double usagePercentage) {
        return usagePercentage >= 80;
    }

    @Override
    public NFKafkaMessage createMessage(long subscNumber, UsageType usageType) {
        return new NFKafkaMessage(
                "80% Threshold Exceeded",
                String.valueOf(subscNumber),
                Instant.now()
        );
    }
}
