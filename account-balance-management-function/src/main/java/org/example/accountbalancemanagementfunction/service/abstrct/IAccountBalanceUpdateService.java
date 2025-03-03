package org.example.accountbalancemanagementfunction.service.abstrct;

import com.ramobeko.kafka.ABMFKafkaMessage;

public interface IAccountBalanceUpdateService {
    void updateBalance(ABMFKafkaMessage message);
}
