package org.example.accountbalancemanagementfunction.strategy.concrete;


import com.ramobeko.dgwtgf.model.UsageType;
import com.ramobeko.oracle.model.OracleBalance;
import org.example.accountbalancemanagementfunction.strategy.abstrct.UsageHandler;
import org.springframework.stereotype.Component;

@Component
public class MinuteUsageHandler implements UsageHandler {

    @Override
    public boolean supports(UsageType usageType) {
        return usageType == UsageType.MINUTE;
    }

    @Override
    public void handle(OracleBalance balance, double usageAmount) {
        if (balance.getLevelMinutes() < usageAmount) {
            throw new RuntimeException("Insufficient minute balance");
        }
        balance.setLevelMinutes(balance.getLevelMinutes() - (long) usageAmount);
        // İsteğe bağlı ek log veya işlem
    }
}

