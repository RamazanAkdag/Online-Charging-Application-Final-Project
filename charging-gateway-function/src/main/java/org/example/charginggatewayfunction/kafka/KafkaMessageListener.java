package org.example.charginggatewayfunction.kafka;

import com.ramobeko.kafka.ABMFKafkaMessage;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.example.charginggatewayfunction.service.abstrct.IChargingService;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaMessageListener implements MessageListener<String, ABMFKafkaMessage> {

    // 🔹 Log4j Logger tanımı
    private static final Logger logger = LogManager.getLogger(KafkaMessageListener.class);

    private final IChargingService chargingService;

    @Override
    public void onMessage(ConsumerRecord<String, ABMFKafkaMessage> record) {
        ABMFKafkaMessage message = record.value();

        // ✅ Mesaj alındı
        logger.info("✅ Kafka'dan mesaj alındı. Topic: {}, Partition: {}, Offset: {}, Key: {}, Value: {}",
                record.topic(), record.partition(), record.offset(), record.key(), message);

        // İş mantığı servisine delege
        try {
            // ✅ Başarılı işlem
            logger.info("✅ Mesaj işleme başarıyla tamamlandı: {}", message);
        } catch (Exception e) {
            // ❌ Hata durumunu logla
            logger.error("❌ Mesaj işlenirken hata oluştu: {}", e.getMessage(), e);
        }
    }
}
