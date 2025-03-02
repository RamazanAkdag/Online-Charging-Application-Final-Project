package org.example.onlinechargingsystem.service.abstrct;

import com.ramobeko.ignite.IgniteBalance;
import org.example.onlinechargingsystem.model.entity.Balance;
public interface IBalanceService {
    IgniteBalance getBalance(Long subscNumber);
    void deductBalanceForMinutes(Long subscNumber, int amount);
    void deductBalanceForSms(Long subscNumber, int amount);

    void deductBalanceForData(Long subscNumber, int amount);

}
