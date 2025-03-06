package org.example.onlinechargingsystem.service.abstrct;

import com.ramobeko.dgwtgf.model.UsageType;
import com.ramobeko.ignite.IgniteBalance;
import org.example.onlinechargingsystem.model.entity.Balance;
public interface IBalanceService {
    IgniteBalance getBalance(Long subscNumber);
    void deductBalance(Long subscNumber, int amount, UsageType usageType);
}
