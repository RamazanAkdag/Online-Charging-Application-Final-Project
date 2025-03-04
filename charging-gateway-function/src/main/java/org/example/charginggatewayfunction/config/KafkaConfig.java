package org.example.charginggatewayfunction.config;

import com.ramobeko.kafka.message.CGFKafkaMessage;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.example.charginggatewayfunction.kafka.KafkaMessageListener;
import org.example.charginggatewayfunction.util.kafka.CGFKafkaMessageDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

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
    public ConsumerFactory<String, CGFKafkaMessage> cgfConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        // CGFKafkaMessage için özel Deserializer
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, CGFKafkaMessageDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public MessageListenerContainer cgfMessageListenerContainer(
            ConsumerFactory<String, CGFKafkaMessage> cgfConsumerFactory,
            KafkaMessageListener kafkaMessageListener) {

        ContainerProperties containerProps = new ContainerProperties(topicName);
        containerProps.setMessageListener(kafkaMessageListener);

        return new ConcurrentMessageListenerContainer<>(cgfConsumerFactory, containerProps);
    }
}
