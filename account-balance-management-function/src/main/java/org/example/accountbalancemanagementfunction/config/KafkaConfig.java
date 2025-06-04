package org.example.accountbalancemanagementfunction.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.example.accountbalancemanagementfunction.kafka.KafkaMessageListener;
import org.example.accountbalancemanagementfunction.util.kafka.ABMFKafkaMessageDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import com.ramobeko.kafka.message.ABMFKafkaMessage;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.*;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.group-id}")
    private String groupId;

    @Value("${kafka.topic}")
    private String topic;

    @Bean
    public ConsumerFactory<String, ABMFKafkaMessage> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ABMFKafkaMessageDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public MessageListenerContainer abmfMessageListenerContainer(
            ConsumerFactory<String, ABMFKafkaMessage> consumerFactory,
            KafkaMessageListener messageListener) {

        ContainerProperties containerProps = new ContainerProperties(topic);
        containerProps.setMessageListener(messageListener);
        return new ConcurrentMessageListenerContainer<>(consumerFactory, containerProps);
    }
}
