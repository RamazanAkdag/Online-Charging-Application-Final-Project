package com.ramobeko.oracle.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "T_NOTIFICATION_LOGS")
public class NotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOTIFICATION_ID", nullable = false)
    private Long notificationId;

    @Column(name = "NOTIFICATION_TYPE", nullable = false, length = 50)
    private String notificationType;

    @Column(name = "NOTIFICATION_TIME", nullable = false)
    private Instant notificationTime;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    // Parametresiz Constructor
    public NotificationLog() {
    }

    // Tüm alanları içeren Constructor
    public NotificationLog(String notificationType, Instant notificationTime, Long customerId) {
        this.notificationType = notificationType;
        this.notificationTime = notificationTime;
        this.customerId = customerId;
    }

    // Getter ve Setter Metotları
    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public Instant getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(Instant notificationTime) {
        this.notificationTime = notificationTime;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "NotificationLog{" +
                "notificationId=" + notificationId +
                ", notificationType='" + notificationType + '\'' +
                ", notificationTime=" + notificationTime +
                ", customerId=" + customerId +
                '}';
    }
}
