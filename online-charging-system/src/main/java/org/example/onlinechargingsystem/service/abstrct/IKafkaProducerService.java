package org.example.onlinechargingsystem.service.abstrct;


import org.example.onlinechargingsystem.model.kafka.KafkaMessage;

public interface IKafkaProducerService {
    void sendUsageData(String topic, KafkaMessage message);
}

