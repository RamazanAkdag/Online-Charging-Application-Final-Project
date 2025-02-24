package org.example.onlinechargingsystem.service.concrete;


import org.example.onlinechargingsystem.model.kafka.KafkaMessage;
import org.example.onlinechargingsystem.service.abstrct.IKafkaProducerService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService implements IKafkaProducerService {

    private final KafkaTemplate<String, KafkaMessage> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, KafkaMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendUsageData(String topic, KafkaMessage message) {
        kafkaTemplate.send(topic, message);
    }
}




