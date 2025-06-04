package org.example.onlinechargingsystem.strategy.balancestrategy.concrete.checker;

import com.ramobeko.dgwtgf.model.UsageType;
import org.example.onlinechargingsystem.strategy.balancestrategy.abstrct.IUsageBalanceChecker;

import java.util.HashMap;
import java.util.Map;

public class UsageBalanceCheckerFactory {
    private static final Map<UsageType, IUsageBalanceChecker> checkerMap = new HashMap<>();

    static {
        checkerMap.put(UsageType.DATA, new DataBalanceChecker());
        checkerMap.put(UsageType.MINUTE, new MinuteBalanceChecker());
        checkerMap.put(UsageType.SMS, new SmsBalanceChecker());
    }

    public static IUsageBalanceChecker getChecker(UsageType usageType) {
        return checkerMap.getOrDefault(usageType, balance -> 0); // Varsayılan olarak 0 döndür (tanımlanmayan kullanım tipi için)
    }
}
