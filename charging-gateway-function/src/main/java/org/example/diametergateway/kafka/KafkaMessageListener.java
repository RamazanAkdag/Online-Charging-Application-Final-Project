package org.example.charginggatewayfunction.kafka;

import com.ramobeko.kafka.message.CGFKafkaMessage;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.charginggatewayfunction.service.abstrct.IChargingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class KafkaMessageListener {

    private static final Logger logger = LogManager.getLogger(KafkaMessageListener.class);

    private final IChargingService chargingService;

    @KafkaListener(topics = "${spring.kafka.template.default-topic}")
    public void listen(CGFKafkaMessage message) {

        logger.info("✅ Kafka'dan mesaj alındı. Value: {}", message);
        chargingService.processCGFMessage(message);
        logger.info("✅ Mesaj işleme başarıyla tamamlandı: {}", message);


    }
}

