package org.example.notificationfunction.config;

import com.ramobeko.kafka.message.NFKafkaMessage;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.example.notificationfunction.kafka.KafkaMessageListener;
import org.example.notificationfunction.util.kafka.NFKafkaMessageDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConfig.class);

    @Bean
    public ConsumerFactory<String, NFKafkaMessage> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "nf_group_id");
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        // Mesajın value'su NFKafkaMessage olduğu için özel Deserializer
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, NFKafkaMessageDeserializer.class);

        logger.info("🔧 Creating ConsumerFactory with properties: {}", configProps);
        ConsumerFactory<String, NFKafkaMessage> factory = new DefaultKafkaConsumerFactory<>(configProps);
        logger.info("✅ ConsumerFactory created successfully.");

        return factory;
    }

    @Bean
    public MessageListenerContainer nfMessageListenerContainer(
            ConsumerFactory<String, NFKafkaMessage> consumerFactory,
            KafkaMessageListener messageListener) {

        logger.info("🔧 Creating ContainerProperties for topic 'nf_topic'");
        ContainerProperties containerProps = new ContainerProperties("nf_topic");
        containerProps.setMessageListener(messageListener);

        logger.info("🔧 Creating ConcurrentMessageListenerContainer for 'nf_topic'");
        ConcurrentMessageListenerContainer<String, NFKafkaMessage> container =
                new ConcurrentMessageListenerContainer<>(consumerFactory, containerProps);

        logger.info("✅ ConcurrentMessageListenerContainer created for topic 'nf_topic'. Ready to start listening.");
        return container;
    }
}
