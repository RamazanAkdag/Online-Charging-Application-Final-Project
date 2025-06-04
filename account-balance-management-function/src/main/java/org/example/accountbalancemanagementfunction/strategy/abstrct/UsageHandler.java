package org.example.accountbalancemanagementfunction.strategy.abstrct;

import com.ramobeko.dgwtgf.model.UsageType;
import com.ramobeko.oracle.model.OracleBalance;

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

