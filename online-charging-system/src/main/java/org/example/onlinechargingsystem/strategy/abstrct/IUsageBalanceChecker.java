package org.example.onlinechargingsystem.strategy.abstrct;

import com.ramobeko.ignite.IgniteBalance;

public interface IUsageBalanceChecker {
    double getAvailableBalance(IgniteBalance balance);
}
