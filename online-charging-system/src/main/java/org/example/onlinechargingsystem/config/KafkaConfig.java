package org.example.onlinechargingsystem.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.example.onlinechargingsystem.util.KafkaMessageSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import java.util.HashMap;
import java.util.Map;
import com.ramobeko.kafka.message.ABMFKafkaMessage; // 🔹 KafkaMessage import et

@Configuration
public class KafkaConfig {

    @Bean
    public ProducerFactory<String, ABMFKafkaMessage> producerFactory() { // 🔹 Burada KafkaMessage kullanıyoruz
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // Kafka broker adresi
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaMessageSerializer.class); // 🔹 Özel Serializer eklendi
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, ABMFKafkaMessage> kafkaTemplate() { // 🔹 Burada KafkaMessage tipiyle değiştirildi
        return new KafkaTemplate<>(producerFactory());
    }
}
