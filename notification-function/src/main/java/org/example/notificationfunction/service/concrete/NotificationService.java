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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NotificationService implements INotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final OracleNotificationLogsRepository notificationLogRepository;
    private final OracleSubscriberRepository subscriberRepository;
    private final EmailService emailService;

    // E-posta içeriği için yapılandırma dosyasından alınan değerler.
    @Value("${notification.email.subject.prefix}")
    private String emailSubjectPrefix;

    @Value("${notification.email.body.template}")
    private String emailBodyTemplate;

    public NotificationService(OracleNotificationLogsRepository notificationLogRepository,
                               OracleSubscriberRepository subscriberRepository,
                               EmailService emailService) {
        this.notificationLogRepository = notificationLogRepository;
        this.subscriberRepository = subscriberRepository;
        this.emailService = emailService;
    }

    @Transactional
    public void saveNotification(NFKafkaMessage kafkaMessage) {
        logger.info("🔍 Mesaj alındı, senderSubscNumber: {}", kafkaMessage.getSenderSubscNumber());

        Optional<OracleSubscriber> optionalSubscriber =
                subscriberRepository.findByPhoneNumber(kafkaMessage.getSenderSubscNumber());

        if (optionalSubscriber.isPresent()) {
            OracleSubscriber subscriber = optionalSubscriber.get();
            OracleCustomer customer = subscriber.getCustomer();
            logger.info("✅ Abone bulundu: {}, customerId: {}", subscriber.getPhoneNumber(), customer.getId());

            NotificationLog notificationLog = NFKafkaMessageMapper.mapToNotificationLog(kafkaMessage);
            notificationLog.setCustomerId(customer.getId());

            notificationLogRepository.save(notificationLog);
            logger.info("✅ Bildirim başarıyla kaydedildi: {}", notificationLog);

            // Müşterinin e-posta adresini al
            String customerEmail = customer.getEmail();
            if (customerEmail != null && !customerEmail.isEmpty()) {
                // Dinamik içerik oluşturmak için şablonda yer alan placeholder'ları değiştirin
                String subject = emailSubjectPrefix + " " + notificationLog.getNotificationType();
                String body = emailBodyTemplate
                        .replace("{notificationType}", notificationLog.getNotificationType())
                        .replace("{notificationTime}", notificationLog.getNotificationTime().toString());

                emailService.sendEmail(customerEmail, subject, body);
                logger.info("✅ E-posta gönderildi: {}", customerEmail);
            } else {
                logger.warn("❌ Müşterinin e-posta adresi bulunamadı. E-posta gönderilemedi.");
            }
        } else {
            logger.warn("❌ Abone bulunamadı. senderSubscNumber: {}", kafkaMessage.getSenderSubscNumber());
        }
    }
}
