package org.example.onlinechargingsystem.service.abstrct;

import org.example.onlinechargingsystem.model.entity.Balance;

public interface IBalanceService {
    Balance getBalance(Long subscriberId);
    void deductBalance(Long subscriberId, int minutes, int sms, int data);
}

