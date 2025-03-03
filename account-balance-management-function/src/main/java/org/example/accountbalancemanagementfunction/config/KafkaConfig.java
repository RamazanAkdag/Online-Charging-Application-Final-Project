package org.example.accountbalancemanagementfunction.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.example.accountbalancemanagementfunction.kafka.KafkaMessageListener;
import org.example.accountbalancemanagementfunction.util.KafkaMessageDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.apache.kafka.common.serialization.StringDeserializer;
import com.ramobeko.kafka.ABMFKafkaMessage;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.*;

import java.util.HashMap;
import java.util.Map;

import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.beans.factory.annotation.Value;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Bean
    public ConsumerFactory<String, ABMFKafkaMessage> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaMessageDeserializer.class);
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
