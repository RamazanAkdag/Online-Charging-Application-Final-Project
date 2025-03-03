package org.example.charginggatewayfunction.config;

import com.ramobeko.kafka.CGFKafkaMessage;
import com.ramobeko.kafka.KafkaMessageDeserializer; // JSON vb. Deserializer
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.example.charginggatewayfunction.kafka.KafkaMessageListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListenerContainer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${kafka.bootstrap.servers}")
    private String bootstrapServers;

    @Value("${kafka.group.id}")
    private String groupId;

    @Value("${kafka.topic.name}")
    private String topicName;

    @Bean
    public ConsumerFactory<String, CGFKafkaMessage> cgwConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // CGWKafkaMessage için özel Deserializer
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaMessageDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public MessageListenerContainer cgwMessageListenerContainer(
            ConsumerFactory<String, CGFKafkaMessage> cgwConsumerFactory,
            KafkaMessageListener kafkaMessageListener) {

        ContainerProperties containerProps = new ContainerProperties(topicName);
        containerProps.setMessageListener(kafkaMessageListener);

        return new ConcurrentMessageListenerContainer<>(cgwConsumerFactory, containerProps);
    }
}
