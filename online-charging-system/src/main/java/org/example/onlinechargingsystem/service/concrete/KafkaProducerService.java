package org.example.onlinechargingsystem.service.concrete;


import com.ramobeko.kafka.message.ABMFKafkaMessage;
import org.example.onlinechargingsystem.service.abstrct.IKafkaProducerService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService implements IKafkaProducerService {

    private final KafkaTemplate<String, ABMFKafkaMessage> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, ABMFKafkaMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendUsageData(String topic, ABMFKafkaMessage message) {
        kafkaTemplate.send(topic, message);
    }
}




