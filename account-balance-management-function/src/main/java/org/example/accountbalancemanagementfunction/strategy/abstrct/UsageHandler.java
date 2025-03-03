package org.example.accountbalancemanagementfunction.strategy.abstrct;

import org.example.accountbalancemanagementfunction.model.oracle.OracleBalance;
import com.ramobeko.dgwtgf.model.UsageType;

public interface UsageHandler {
    /**
     * Bu handler hangi UsageType'ı destekliyor?
     */
    boolean supports(UsageType usageType);

    /**
     * UsageType'a özel kullanım mantığı bu metotta yer alır.
     * Yetersiz bakiye vb. durumlar için exception fırlatılabilir.
     */
    void handle(OracleBalance balance, double usageAmount);
}

