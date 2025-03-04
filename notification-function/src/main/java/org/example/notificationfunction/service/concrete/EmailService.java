package org.example.notificationfunction.service.concrete;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String to, String subject, String body) {
        // Basit bir e-posta mesajı oluştur
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@bekoramoonline.com");  // E-posta gönderici adresi
        message.setTo(to);  // Alıcı adresi
        message.setSubject(subject);  // Konu
        message.setText(body);  // Mesaj içeriği

        // E-posta gönder
        javaMailSender.send(message);

        // E-posta gönderildiğinde loglama
        System.out.println("Email sent to: " + to);
    }
}

