package org.example.onlinechargingsystem.strategy.concrete;

import com.ramobeko.dgwtgf.model.UsageType;
import org.example.onlinechargingsystem.strategy.abstrct.IUsageDeductionStrategy;

import java.util.HashMap;
import java.util.Map;

public class UsageDeductionStrategyFactory {
    private static final Map<UsageType, IUsageDeductionStrategy> strategyMap = new HashMap<>();

    static {
        strategyMap.put(UsageType.MINUTE, new MinuteDeductionStrategy());
        strategyMap.put(UsageType.SMS, new SMSDeductionStrategy());
        strategyMap.put(UsageType.DATA, new DataDeductionStrategy());
    }

    public static IUsageDeductionStrategy getStrategy(UsageType usageType) {
        return strategyMap.getOrDefault(usageType, (subscriber, amount, repository, subscNumber) -> {
            throw new IllegalArgumentException("Unknown usage type: " + usageType);
        });
    }
}
