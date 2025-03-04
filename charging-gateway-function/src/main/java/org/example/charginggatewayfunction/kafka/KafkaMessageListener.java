package org.example.charginggatewayfunction.kafka;

import com.ramobeko.kafka.message.CGFKafkaMessage;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.charginggatewayfunction.service.abstrct.IChargingService;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaMessageListener implements MessageListener<String, CGFKafkaMessage> {

    private static final Logger logger = LogManager.getLogger(KafkaMessageListener.class);

    private final IChargingService chargingService;

    @Override
    public void onMessage(ConsumerRecord<String, CGFKafkaMessage> record) {
        CGFKafkaMessage message = record.value();

        // ✅ Mesaj alındı
        logger.info("✅ Kafka'dan mesaj alındı. Topic: {}, Partition: {}, Offset: {}, Key: {}, Value: {}",
                record.topic(), record.partition(), record.offset(), record.key(), message);

        try {
            // Servis katmanındaki iş mantığı metodu
            chargingService.processCGFMessage(message);

            // ✅ Başarılı işlem
            logger.info("✅ Mesaj işleme başarıyla tamamlandı: {}", message);
        } catch (Exception e) {
            // ❌ Hata durumunu logla
            logger.error("❌ Mesaj işlenirken hata oluştu: {}", e.getMessage(), e);
        }
    }
}
