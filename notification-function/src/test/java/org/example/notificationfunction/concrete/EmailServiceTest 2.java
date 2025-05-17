package org.example.notificationfunction.concrete;

import org.example.notificationfunction.service.concrete.EmailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailService emailService;

    @Captor
    private ArgumentCaptor<SimpleMailMessage> messageCaptor;

    @Test
    @DisplayName("should send email with correct details")
    void testSendEmail() {
        // Arrange
        String to = "test@example.com";
        String subject = "Test Subject";
        String body = "Test email body.";

        // Act
        emailService.sendEmail(to, subject, body);

        // Assert
        verify(javaMailSender).send(messageCaptor.capture());
        SimpleMailMessage sentMessage = messageCaptor.getValue();

        // İçeriği kontrol edelim:
        assertEquals("no-reply@bekoramoonline.com", sentMessage.getFrom());
        assertEquals(to, sentMessage.getTo()[0]);  // To bir dizi
        assertEquals(subject, sentMessage.getSubject());
        assertEquals(body, sentMessage.getText());
    }
}

