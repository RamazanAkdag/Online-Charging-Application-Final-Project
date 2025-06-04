package org.example.notificationfunction.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.example.notificationfunction.service.abstrct.INotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.ramobeko.kafka.message.NFKafkaMessage;

@Component
@RequiredArgsConstructor
public class KafkaMessageListener {

    private static final Logger logger = LogManager.getLogger(KafkaMessageListener.class);

    private final INotificationService notificationService;

    @KafkaListener(topics = "${spring.kafka.topic.nf-topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(NFKafkaMessage message) {
        try {
            logger.info("✅ Kafka'dan mesaj alındı. Value: {}", message);

            // NotificationService ile bildirimi işle
            notificationService.saveNotification(message);

            logger.info("✅ Mesaj işleme başarıyla tamamlandı: {}", message);
        } catch (Exception e) {
            logger.error("❌ Mesaj işlenirken hata oluştu. Hata: {}", e.getMessage(), e);
        }
    }
}
