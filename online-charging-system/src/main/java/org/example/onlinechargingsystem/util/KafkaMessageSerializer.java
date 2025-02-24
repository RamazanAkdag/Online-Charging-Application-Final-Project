package org.example.onlinechargingsystem.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import org.example.onlinechargingsystem.model.kafka.KafkaMessage;

public class KafkaMessageSerializer implements Serializer<KafkaMessage> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, KafkaMessage data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException("KafkaMessage serialization error", e);
        }
    }
}

