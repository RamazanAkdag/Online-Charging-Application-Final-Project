package org.example.accountbalancemanagementfunction.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.example.accountbalancemanagementfunction.kafka.KafkaMessageListener;
import org.example.accountbalancemanagementfunction.util.kafka.ABMFKafkaMessageDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import com.ramobeko.kafka.message.ABMFKafkaMessage;
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
    public ConsumerFactory<String, ABMFKafkaMessage> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "abmf_group_id");
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // Mesajın value'su ABMFKafkaMessage olduğu için özel Deserializer
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ABMFKafkaMessageDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public MessageListenerContainer abmfMessageListenerContainer(
            ConsumerFactory<String, ABMFKafkaMessage> consumerFactory,
            KafkaMessageListener messageListener) {

        ContainerProperties containerProps = new ContainerProperties("usage-events");
        containerProps.setMessageListener(messageListener);

        ConcurrentMessageListenerContainer<String, ABMFKafkaMessage> container =
                new ConcurrentMessageListenerContainer<>(consumerFactory, containerProps);

        return container;
    }
}
