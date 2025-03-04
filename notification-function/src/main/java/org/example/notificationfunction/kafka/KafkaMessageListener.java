package org.example.notificationfunction.kafka;

import com.ramobeko.kafka.message.NFKafkaMessage;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.notificationfunction.service.abstrct.INotificationService;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaMessageListener implements MessageListener<String, NFKafkaMessage> {

    private static final Logger logger = LogManager.getLogger(KafkaMessageListener.class);

    // NotificationService servisini constructor injection ile enjekte ediyoruz
    private final INotificationService notificationService;

    @Override
    public void onMessage(ConsumerRecord<String, NFKafkaMessage> record) {
        NFKafkaMessage message = record.value();

        try {
            logger.info("✅ Kafka'dan mesaj alındı. Topic: {}, Partition: {}, Offset: {}, Key: {}, Value: {}",
                    record.topic(), record.partition(), record.offset(), record.key(), message);

            // NotificationService'i çağırarak bildirimi kaydet
            notificationService.saveNotification(message);

            logger.info("✅ Mesaj işleme başarıyla tamamlandı: {}", message);
        } catch (Exception e) {
            logger.error("❌ Mesaj işlenirken hata oluştu. Hata: {}", e.getMessage(), e);
        }
    }
}
