package org.example.accountbalancemanagementfunction.util.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ramobeko.kafka.message.ABMFKafkaMessage;
import org.apache.kafka.common.serialization.Deserializer;

public class ABMFKafkaMessageDeserializer implements Deserializer<ABMFKafkaMessage> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ABMFKafkaMessage deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, ABMFKafkaMessage.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

