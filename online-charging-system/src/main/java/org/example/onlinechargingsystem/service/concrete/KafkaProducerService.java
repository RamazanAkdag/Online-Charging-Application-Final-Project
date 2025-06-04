package org.example.onlinechargingsystem.service.concrete;

import com.ramobeko.kafka.message.ABMFKafkaMessage;
import com.ramobeko.kafka.message.CGFKafkaMessage;
import com.ramobeko.kafka.message.NFKafkaMessage;
import org.example.onlinechargingsystem.service.abstrct.IKafkaProducerService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService implements IKafkaProducerService {

    private final KafkaTemplate<String, ABMFKafkaMessage> abmfKafkaTemplate;
    private final KafkaTemplate<String, CGFKafkaMessage> cgfKafkaTemplate;
    private final KafkaTemplate<String, NFKafkaMessage> nfKafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, ABMFKafkaMessage> abmfKafkaTemplate,
                                KafkaTemplate<String, CGFKafkaMessage> cgfKafkaTemplate,
                                KafkaTemplate<String, NFKafkaMessage> nfKafkaTemplate) {
        this.abmfKafkaTemplate = abmfKafkaTemplate;
        this.cgfKafkaTemplate = cgfKafkaTemplate;
        this.nfKafkaTemplate = nfKafkaTemplate;
    }

    @Override
    public void sendABMFUsageData(String topic, ABMFKafkaMessage message) {
        abmfKafkaTemplate.send(topic, message);
    }

    @Override
    public void sendCGFUsageData(String topic, CGFKafkaMessage message) {
        cgfKafkaTemplate.send(topic, message);
    }

    @Override
    public void sendNotificationData(String topic, NFKafkaMessage message) {
        nfKafkaTemplate.send(topic, message);
    }
}
