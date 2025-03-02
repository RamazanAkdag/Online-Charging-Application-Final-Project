package org.example.onlinechargingsystem.service.abstrct;


import com.ramobeko.kafka.ABMFKafkaMessage;

public interface IKafkaProducerService {
    void sendUsageData(String topic, ABMFKafkaMessage message);
}

