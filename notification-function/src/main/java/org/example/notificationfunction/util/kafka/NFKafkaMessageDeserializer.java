package org.example.notificationfunction.util.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ramobeko.kafka.message.NFKafkaMessage;
import org.apache.kafka.common.serialization.Deserializer;

public class NFKafkaMessageDeserializer   implements Deserializer<NFKafkaMessage> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public NFKafkaMessage deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, NFKafkaMessage.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
