package org.example.onlinechargingsystem.strategy.thresholdstrategy.concrete;

import com.ramobeko.dgwtgf.model.UsageType;
import com.ramobeko.kafka.message.NFKafkaMessage;
import org.example.onlinechargingsystem.strategy.thresholdstrategy.abstrct.UsageThresholdRule;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UsageThresholdPolicy {

    private final List<UsageThresholdRule> rules;

    public UsageThresholdPolicy(List<UsageThresholdRule> rules) {
        this.rules = rules;
    }

    public Optional<NFKafkaMessage> evaluate(long subscNumber, UsageType usageType, double usagePercentage) {
        return rules.stream()
                .filter(rule -> rule.isApplicable(usagePercentage))
                .findFirst()
                .map(rule -> rule.createMessage(subscNumber, usageType));
    }
}
