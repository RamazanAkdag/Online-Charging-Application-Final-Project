package org.example.onlinechargingsystem.strategy.balancestrategy.concrete.checker;

import com.ramobeko.ignite.IgniteBalance;
import org.example.onlinechargingsystem.strategy.balancestrategy.abstrct.IUsageBalanceChecker;

public class SmsBalanceChecker implements IUsageBalanceChecker {
    @Override
    public double getAvailableBalance(IgniteBalance balance) {
        return balance.getLevelSms();
    }
}
