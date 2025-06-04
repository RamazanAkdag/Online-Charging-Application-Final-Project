package org.example.onlinechargingsystem.service.abstrct;


import com.ramobeko.kafka.message.ABMFKafkaMessage;
import com.ramobeko.kafka.message.CGFKafkaMessage;
import com.ramobeko.kafka.message.NFKafkaMessage;

public interface IKafkaProducerService {
    public void sendABMFUsageData(String topic, ABMFKafkaMessage message);
    public void sendCGFUsageData(String topic, CGFKafkaMessage message);
    public void sendNotificationData(String topic, NFKafkaMessage message);
}

