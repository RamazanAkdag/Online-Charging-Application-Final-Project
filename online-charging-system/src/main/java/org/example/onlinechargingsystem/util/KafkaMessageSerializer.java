package org.example.onlinechargingsystem.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import com.ramobeko.kafka.ABMFKafkaMessage;

public class KafkaMessageSerializer implements Serializer<ABMFKafkaMessage> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, ABMFKafkaMessage data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException("KafkaMessage serialization error", e);
        }
    }
}

