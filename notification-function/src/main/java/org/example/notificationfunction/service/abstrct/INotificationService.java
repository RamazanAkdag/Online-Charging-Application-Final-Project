package org.example.notificationfunction.service.abstrct;

import com.ramobeko.kafka.message.NFKafkaMessage;

public interface INotificationService {
    public void saveNotification(NFKafkaMessage kafkaMessage);
}
