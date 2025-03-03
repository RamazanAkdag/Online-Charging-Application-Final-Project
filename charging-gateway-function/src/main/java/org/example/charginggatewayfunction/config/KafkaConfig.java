package org.example.charginggatewayfunction.config;

import com.ramobeko.kafka.ABMFKafkaMessage;
import com.ramobeko.kafka.KafkaMessageDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.example.charginggatewayfunction.kafka.KafkaMessageListener;
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

    @Bean
    public ConsumerFactory<String, ABMFKafkaMessage> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "cgf_group_id");
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // Mesajın value'su ABMFKafkaMessage olduğu için özel Deserializer
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaMessageDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public MessageListenerContainer cgfMessageListenerContainer(
            ConsumerFactory<String, ABMFKafkaMessage> consumerFactory,
            KafkaMessageListener messageListener) {

        ContainerProperties containerProps = new ContainerProperties("cgf_topic");
        containerProps.setMessageListener(messageListener);

        ConcurrentMessageListenerContainer<String, ABMFKafkaMessage> container =
                new ConcurrentMessageListenerContainer<>(consumerFactory, containerProps);

        return container;
    }
}
