package org.example.onlinechargingsystem.strategy.thresholdstrategy.concrete.rules;

import com.ramobeko.dgwtgf.model.UsageType;
import com.ramobeko.kafka.message.NFKafkaMessage;
import org.example.onlinechargingsystem.strategy.thresholdstrategy.abstrct.UsageThresholdRule;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Order(2) // Önce 100%
@Component
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
