package org.example.accountbalancemanagementfunction.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.accountbalancemanagementfunction.service.abstrct.IAccountBalanceUpdateService;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

import com.ramobeko.kafka.message.ABMFKafkaMessage;
import lombok.RequiredArgsConstructor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Component
@RequiredArgsConstructor
public class KafkaMessageListener implements MessageListener<String, ABMFKafkaMessage> {

    private static final Logger logger = LogManager.getLogger(KafkaMessageListener.class);

    private final IAccountBalanceUpdateService accountBalanceUpdateService;

    @Override
    public void onMessage(ConsumerRecord<String, ABMFKafkaMessage> record) {
        ABMFKafkaMessage message = record.value();

        logger.info("✅ Kafka'dan mesaj alındı. Topic: {}, Partition: {}, Offset: {}, Key: {}, Value: {}",
                record.topic(), record.partition(), record.offset(), record.key(), message);

        try {
            accountBalanceUpdateService.updateBalance(message);
            logger.info("✅ Mesaj işleme başarıyla tamamlandı: {}", message);
        } catch (Exception e) {
            logger.error("❌ Mesaj işlenirken hata oluştu: {}", e.getMessage(), e);
        }
    }
}
