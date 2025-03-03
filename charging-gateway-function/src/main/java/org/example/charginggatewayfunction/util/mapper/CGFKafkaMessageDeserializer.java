package org.example.charginggatewayfunction.util.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ramobeko.kafka.CGFKafkaMessage;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class CGFKafkaMessageDeserializer implements Deserializer<CGFKafkaMessage> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // Opsiyonel konfigürasyon
    }

    @Override
    public CGFKafkaMessage deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }
        try {
            // Gelen JSON byte[] verisini CGFKafkaMessage nesnesine dönüştürür
            return objectMapper.readValue(data, CGFKafkaMessage.class);
        } catch (Exception e) {
            throw new SerializationException("Error deserializing CGFKafkaMessage", e);
        }
    }

    @Override
    public void close() {
        // Kaynak temizliği gerekiyorsa burada yapılır
    }
}
