package org.example.accountbalancemanagementfunction.strategy.concrete;


import com.ramobeko.dgwtgf.model.UsageType;
import com.ramobeko.oracle.model.OracleBalance;
import org.example.accountbalancemanagementfunction.strategy.abstrct.UsageHandler;
import org.springframework.stereotype.Component;

@Component
public class DataUsageHandler implements UsageHandler {

    @Override
    public boolean supports(UsageType usageType) {
        return usageType == UsageType.DATA;
    }

    @Override
    public void handle(OracleBalance balance, double usageAmount) {
        if (balance.getLevelData() < usageAmount) {
            throw new RuntimeException("Insufficient data balance");
        }
        balance.setLevelData(balance.getLevelData() - (long) usageAmount);
        // İsteğe bağlı ek log veya işlem
    }
}

