package org.example.notificationfunction.util.mapper;

import com.ramobeko.kafka.message.NFKafkaMessage;
import com.ramobeko.oracle.model.NotificationLog;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NFKafkaMessageMapper {

    public static NotificationLog mapToNotificationLog(NFKafkaMessage kafkaMessage) {
        // Kafka mesajındaki verileri al
        String notificationType = kafkaMessage.getNotificationType();
        String notificationTime = kafkaMessage.getNotificationTime();

        // notificationTime'ı String'den LocalDateTime'a dönüştür
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

        LocalDateTime localDateTime = LocalDateTime.parse(notificationTime, formatter);


        // Yeni bir NotificationLog nesnesi oluştur
        NotificationLog notificationLog = new NotificationLog();
        notificationLog.setNotificationType(notificationType);
        notificationLog.setNotificationTime(localDateTime);  // LocalDateTime olarak set ediyoruz

        return notificationLog;
    }
}
