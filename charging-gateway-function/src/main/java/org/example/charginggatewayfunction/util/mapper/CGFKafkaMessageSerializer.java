package org.example.charginggatewayfunction.util.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ramobeko.kafka.CGFKafkaMessage;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class CGFKafkaMessageSerializer implements Serializer<CGFKafkaMessage> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // Opsiyonel konfigürasyon
    }

    @Override
    public byte[] serialize(String topic, CGFKafkaMessage data) {
        if (data == null) {
            return null;
        }
        try {
            // CGFKafkaMessage nesnesini JSON byte[] şeklinde dönüştürür
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new SerializationException("Error serializing CGFKafkaMessage", e);
        }
    }

    @Override
    public void close() {
        // Kaynak temizliği gerekiyorsa burada yapılır
    }
}
