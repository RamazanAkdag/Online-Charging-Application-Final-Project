package org.example.charginggatewayfunction.util.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ramobeko.kafka.message.CGFKafkaMessage;
import org.apache.kafka.common.serialization.Deserializer;

public class CGFKafkaMessageDeserializer implements Deserializer<CGFKafkaMessage> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public CGFKafkaMessage deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, CGFKafkaMessage.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
