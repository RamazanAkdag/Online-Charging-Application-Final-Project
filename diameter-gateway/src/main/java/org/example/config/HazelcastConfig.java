package org.example.config;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;


import com.hazelcast.client.HazelcastClient;

@Configuration
public class HazelcastConfig {

    private static final Logger logger = LogManager.getLogger(HazelcastConfig.class);

    @Bean
    @Primary
    public HazelcastInstance primaryHazelcastInstance() {
        logger.info("Initializing Hazelcast client instance using hazelcast-client.yaml...");
        HazelcastInstance instance = HazelcastClient.newHazelcastClient();
        logger.info("Hazelcast client instance initialized successfully.");
        return instance;
    }
}

