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

import com.ramobeko.kafka.message.ABMFKafkaMessage;
import com.ramobeko.kafka.message.CGFKafkaMessage;
import com.ramobeko.kafka.message.NFKafkaMessage;

@Configuration
public class KafkaConfig {

    private Map<String, Object> producerConfigs() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return configProps;
    }

    @Bean
    public ProducerFactory<String, ABMFKafkaMessage> abmfProducerFactory() {
        Map<String, Object> configProps = new HashMap<>(producerConfigs());
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaMessageSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public ProducerFactory<String, CGFKafkaMessage> cgfProducerFactory() {
        Map<String, Object> configProps = new HashMap<>(producerConfigs());
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaMessageSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public ProducerFactory<String, NFKafkaMessage> nfProducerFactory() {
        Map<String, Object> configProps = new HashMap<>(producerConfigs());
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaMessageSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, ABMFKafkaMessage> abmfKafkaTemplate() {
        return new KafkaTemplate<>(abmfProducerFactory());
    }

    @Bean
    public KafkaTemplate<String, CGFKafkaMessage> cgfKafkaTemplate() {
        return new KafkaTemplate<>(cgfProducerFactory());
    }

    @Bean
    public KafkaTemplate<String, NFKafkaMessage> nfKafkaTemplate() {
        return new KafkaTemplate<>(nfProducerFactory());
    }
}
