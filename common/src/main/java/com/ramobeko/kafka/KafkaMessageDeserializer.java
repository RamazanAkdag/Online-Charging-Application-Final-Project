package com.ramobeko.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

public class KafkaMessageDeserializer implements Deserializer<ABMFKafkaMessage> {

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

