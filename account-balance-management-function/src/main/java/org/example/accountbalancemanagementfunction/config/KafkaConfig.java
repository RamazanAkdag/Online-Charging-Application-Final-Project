package org.example.accountbalancemanagementfunction.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.example.accountbalancemanagementfunction.kafka.KafkaMessageListener;
import org.example.accountbalancemanagementfunction.model.KafkaMessage;
import org.example.accountbalancemanagementfunction.util.KafkaMessageDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.*;

import java.util.HashMap;
import java.util.Map;

import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.MessageListenerContainer;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Bean
    public ConsumerFactory<String, KafkaMessage> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // Kafka broker adresi
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "group_id"); // Consumer group ID
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaMessageDeserializer.class); // Özel Deserializer kullanıldı
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public MessageListenerContainer messageListenerContainer(ConsumerFactory<String, KafkaMessage> consumerFactory) {
        // Container properties tanımlanıyor
        ContainerProperties containerProps = new ContainerProperties("abmf_topic"); // Kafka topic adı
        containerProps.setMessageListener(new KafkaMessageListener()); // KafkaMessageListener burada kullanılıyor

        // Listener container
        ConcurrentMessageListenerContainer<String, KafkaMessage> container = new ConcurrentMessageListenerContainer<>(consumerFactory, containerProps);

        return container;
    }
}

