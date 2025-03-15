package org.example.notificationfunction.util.mapper;

import com.ramobeko.kafka.message.NFKafkaMessage;
import com.ramobeko.oracle.model.NotificationLog;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.ramobeko.kafka.message.NFKafkaMessage;
import com.ramobeko.oracle.model.NotificationLog;

import java.time.Instant;
import java.time.format.DateTimeParseException;

public class NFKafkaMessageMapper {

    public static NotificationLog mapToNotificationLog(NFKafkaMessage kafkaMessage) {
        NotificationLog notificationLog = new NotificationLog();

        try {
            String notificationType = kafkaMessage.getNotificationType();
            Instant notificationTime = kafkaMessage.getNotificationTime();

            notificationLog.setNotificationType(notificationType);
            notificationLog.setNotificationTime(notificationTime);

        } catch (DateTimeParseException e) {
            throw new RuntimeException("❌ Tarih formatı hatalı: " + kafkaMessage.getNotificationTime(), e);
        }

        return notificationLog;
    }
}
