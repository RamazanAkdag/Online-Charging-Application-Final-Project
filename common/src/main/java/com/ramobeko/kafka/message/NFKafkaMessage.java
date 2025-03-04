package com.ramobeko.kafka.message;

import org.apache.kafka.common.serialization.Serializer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;

public class NFKafkaMessage {

    private String notificationType;
    private String senderSubscNumber;
    private String notificationTime;


    // Parametresiz constructor (Jackson için)
    public NFKafkaMessage() {
    }
    // Constructor
    public NFKafkaMessage(String notificationType, String senderSubscNumber, String notificationTime) {

        this.notificationType = notificationType;
        this.senderSubscNumber = senderSubscNumber;
        this.notificationTime = notificationTime;
    }

    // Getters and Setters




    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getSenderSubscNumber() {
        return senderSubscNumber;
    }

    public void setgetSenderSubscNumber(String senderSubscNumber) {
        this.senderSubscNumber = senderSubscNumber;
    }

    public String getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(String notificationTime) {
        this.notificationTime = notificationTime;
    }

    // Method to create Kafka message key


    // Method to create Kafka message value (JSON or any other format)
    public String getKafkaMessageValue() {
        return "{" +
                "\"notificationType\":\"" + notificationType + "\"," +
                "\"customerId\":" + senderSubscNumber + "," +
                "\"notificationTime\":\"" + notificationTime + "\"" +
                "}";
    }

    @Override
    public String toString() {
        return "NFKafkaMessage{" +
                ", notificationType='" + notificationType + '\'' +
                ", senderId=" + senderSubscNumber +
                ", notificationTime='" + notificationTime + '\'' +
                '}';
    }


}

