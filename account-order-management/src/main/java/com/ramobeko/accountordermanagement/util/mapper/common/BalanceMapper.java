package com.ramobeko.accountordermanagement.util.mapper.common;

import com.ramobeko.accountordermanagement.model.entity.oracle.OracleBalance;
import com.ramobeko.accountordermanagement.model.entity.oracle.OraclePackage;
import com.ramobeko.accountordermanagement.model.shared.OracleSubscriber;

import java.util.Date;

public class BalanceMapper {

    public static OracleBalance createFromPackage(OracleSubscriber subscriber, OraclePackage packagePlan) {
        OracleBalance balance = new OracleBalance();
        balance.setSubscriber(subscriber);
        balance.setPackagePlan(packagePlan);
        balance.setLevelMinutes(packagePlan.getAmountMinutes());
        balance.setLevelSms(packagePlan.getAmountSms());
        balance.setLevelData(packagePlan.getAmountData());
        balance.setStartDate(new Date());
        balance.setEndDate(null);
        return balance;
    }
}

