package org.example.notificationfunction.service.concrete;

import com.ramobeko.kafka.message.NFKafkaMessage;
import com.ramobeko.oracle.model.NotificationLog;
import com.ramobeko.oracle.model.OracleCustomer;
import com.ramobeko.oracle.model.OracleSubscriber;
import jakarta.transaction.Transactional;
import org.example.notificationfunction.repository.OracleNotificationLogsRepository;
import org.example.notificationfunction.repository.OracleSubscriberRepository;
import org.example.notificationfunction.service.abstrct.INotificationService;
import org.example.notificationfunction.util.mapper.NFKafkaMessageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NotificationService implements INotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final OracleNotificationLogsRepository notificationLogRepository;
    private final OracleSubscriberRepository subscriberRepository;
    private final EmailService emailService;

    public NotificationService(OracleNotificationLogsRepository notificationLogRepository,
                               OracleSubscriberRepository subscriberRepository,
                               EmailService emailService) {
        this.notificationLogRepository = notificationLogRepository;
        this.subscriberRepository = subscriberRepository;
        this.emailService = emailService;
    }

    @Transactional
    public void saveNotification(NFKafkaMessage kafkaMessage) {
        // 1. Kafka mesajındaki senderSubscNumber'ı kullanarak abone bilgisini al
        logger.info("🔍 Mesaj alındı, senderSubscNumber: {}", kafkaMessage.getSenderSubscNumber());

        Optional<OracleSubscriber> optionalSubscriber =
                subscriberRepository.findByPhoneNumber(kafkaMessage.getSenderSubscNumber());

        if (optionalSubscriber.isPresent()) {
            OracleSubscriber subscriber = optionalSubscriber.get();
            OracleCustomer customer = subscriber.getCustomer();
            logger.info("✅ Abone bulundu: {}, customerId: {}", subscriber.getPhoneNumber(), customer.getId());

            // 2. Kafka mesajını NotificationLog nesnesine dönüştür
            NotificationLog notificationLog = NFKafkaMessageMapper.mapToNotificationLog(kafkaMessage);
            notificationLog.setCustomerId(customer.getId());

            // 3. NotificationLog'u veritabanına kaydet
            notificationLogRepository.save(notificationLog);
            logger.info("✅ Bildirim başarıyla kaydedildi: {}", notificationLog);

            // 4. Müşterinin e-posta adresini doğrudan customer nesnesinden al
            String customerEmail = customer.getEmail();
            if (customerEmail != null && !customerEmail.isEmpty()) {
                // 5. E-posta gönder
                String subject = "Yeni Bildirim: " + notificationLog.getNotificationType();
                String body = "Merhaba,\n\n" +
                        "Yeni bir bildiriminiz var. Detaylar:\n" +
                        "Tip: " + notificationLog.getNotificationType() + "\n" +
                        "Zaman: " + notificationLog.getNotificationTime() + "\n\n" +
                        "İyi günler dileriz.";

                emailService.sendEmail(customerEmail, subject, body);
                logger.info("✅ E-posta gönderildi: {}", customerEmail);
            } else {
                logger.warn("❌ Müşterinin e-posta adresi bulunamadı. E-posta gönderilemedi.");
            }
        } else {
            // Abone bulunamadığında loglama
            logger.warn("❌ Abone bulunamadı. senderSubscNumber: {}", kafkaMessage.getSenderSubscNumber());
        }
    }
}
