package org.example.accountbalancemanagementfunction.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.example.accountbalancemanagementfunction.model.KafkaMessage;

public class KafkaMessageDeserializer implements Deserializer<KafkaMessage> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public KafkaMessage deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, KafkaMessage.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

