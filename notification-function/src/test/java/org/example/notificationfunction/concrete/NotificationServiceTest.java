package org.example.notificationfunction.concrete;

import com.ramobeko.kafka.message.NFKafkaMessage;
import com.ramobeko.oracle.model.NotificationLog;
import com.ramobeko.oracle.model.OracleCustomer;
import com.ramobeko.oracle.model.OracleSubscriber;
import org.example.notificationfunction.repository.OracleNotificationLogsRepository;
import org.example.notificationfunction.repository.OracleSubscriberRepository;
import org.example.notificationfunction.service.concrete.EmailService;
import org.example.notificationfunction.service.concrete.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private OracleNotificationLogsRepository notificationLogRepository;

    @Mock
    private OracleSubscriberRepository subscriberRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private NotificationService notificationService;

    private NFKafkaMessage kafkaMessage;

    @BeforeEach
    void setup() {
        kafkaMessage = new NFKafkaMessage();
        kafkaMessage.setSenderSubscNumber("500");
        kafkaMessage.setNotificationType("BALANCE_ALERT");
        kafkaMessage.setNotificationTime(Instant.now()); // ARTIK Instant

        // @Value alanlarını elle set ediyoruz:
        ReflectionTestUtils.setField(notificationService, "emailSubjectPrefix", "[Bildirim]");
        ReflectionTestUtils.setField(notificationService, "emailBodyTemplate", "Tip: {notificationType}, Zaman: {notificationTime}");
    }

    @Test
    @DisplayName("should save notification and send email if subscriber and email exist")
    void testSaveNotification_withEmail() {
        // Arrange
        OracleCustomer customer = new OracleCustomer();
        customer.setId(1L);
        customer.setEmail("customer@example.com");

        OracleSubscriber subscriber = new OracleSubscriber();
        subscriber.setPhoneNumber("500");
        subscriber.setCustomer(customer);

        when(subscriberRepository.findByPhoneNumber("500")).thenReturn(Optional.of(subscriber));

        // Act
        notificationService.saveNotification(kafkaMessage);

        // Assert
        verify(notificationLogRepository).save(any(NotificationLog.class));

        verify(emailService).sendEmail(
                eq("customer@example.com"),
                eq("[Bildirim] BALANCE_ALERT"),
                contains("BALANCE_ALERT")
        );
    }

    @Test
    @DisplayName("should save notification but not send email if email is missing")
    void testSaveNotification_withoutEmail() {
        // Arrange
        OracleCustomer customer = new OracleCustomer();
        customer.setId(1L);
        customer.setEmail(null); // email yok

        OracleSubscriber subscriber = new OracleSubscriber();
        subscriber.setPhoneNumber("500");
        subscriber.setCustomer(customer);

        when(subscriberRepository.findByPhoneNumber("500")).thenReturn(Optional.of(subscriber));

        // Act
        notificationService.saveNotification(kafkaMessage);

        // Assert
        verify(notificationLogRepository).save(any(NotificationLog.class));
        verify(emailService, never()).sendEmail(any(), any(), any());
    }

    @Test
    @DisplayName("should not save notification or send email if subscriber not found")
    void testSaveNotification_subscriberNotFound() {
        // Arrange
        when(subscriberRepository.findByPhoneNumber("500")).thenReturn(Optional.empty());

        // Act
        notificationService.saveNotification(kafkaMessage);

        // Assert
        verify(notificationLogRepository, never()).save(any());
        verify(emailService, never()).sendEmail(any(), any(), any());
    }
}
