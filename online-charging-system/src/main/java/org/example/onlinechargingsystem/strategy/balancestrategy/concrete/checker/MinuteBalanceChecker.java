package org.example.onlinechargingsystem.strategy.concrete.checker;

import com.ramobeko.ignite.IgniteBalance;
import org.example.onlinechargingsystem.strategy.abstrct.IUsageBalanceChecker;

public class MinuteBalanceChecker implements IUsageBalanceChecker {
    @Override
    public double getAvailableBalance(IgniteBalance balance) {
        return balance.getLevelMinutes();
    }
}
