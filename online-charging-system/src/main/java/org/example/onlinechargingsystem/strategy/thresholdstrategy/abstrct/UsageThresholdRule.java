package org.example.onlinechargingsystem.strategy.thresholdstrategy.abstrct;

import com.ramobeko.dgwtgf.model.UsageType;
import com.ramobeko.kafka.message.NFKafkaMessage;

public interface UsageThresholdRule {
    boolean isApplicable(double usagePercentage);
    NFKafkaMessage createMessage(long subscNumber, UsageType usageType);
}

