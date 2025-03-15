package com.ramobeko.kafka.message;

import java.time.Instant;

public class NFKafkaMessage {

    private String notificationType;
    private String senderSubscNumber;
    private Instant notificationTime; // Instant olarak saklanıyor

    public NFKafkaMessage() {
    }

    public NFKafkaMessage(String notificationType, String senderSubscNumber, Instant notificationTime) {
        this.notificationType = notificationType;
        this.senderSubscNumber = senderSubscNumber;
        this.notificationTime = notificationTime;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getSenderSubscNumber() {
        return senderSubscNumber;
    }

    public void setSenderSubscNumber(String senderSubscNumber) {
        this.senderSubscNumber = senderSubscNumber;
    }

    public Instant getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(Instant notificationTime) {
        this.notificationTime = notificationTime;
    }

    @Override
    public String toString() {
        return "NFKafkaMessage{" +
                "notificationType='" + notificationType + '\'' +
                ", senderSubscNumber='" + senderSubscNumber + '\'' +
                ", notificationTime=" + notificationTime +
                '}';
    }
}
