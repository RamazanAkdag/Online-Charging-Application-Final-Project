package org.example.accountbalancemanagementfunction.strategy.concrete;


import org.example.accountbalancemanagementfunction.model.oracle.OracleBalance;
import com.ramobeko.dgwtgf.model.UsageType;
import org.example.accountbalancemanagementfunction.strategy.abstrct.UsageHandler;
import org.springframework.stereotype.Component;

@Component
public class SmsUsageHandler implements UsageHandler {

    @Override
    public boolean supports(UsageType usageType) {
        return usageType == UsageType.SMS;
    }

    @Override
    public void handle(OracleBalance balance, double usageAmount) {
        if (balance.getLevelSms() < usageAmount) {
            throw new RuntimeException("Insufficient SMS balance");
        }
        balance.setLevelSms(balance.getLevelSms() - (long) usageAmount);
        // İsteğe bağlı ek log veya işlem
    }
}

