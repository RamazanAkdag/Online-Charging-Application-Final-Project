package com.ramobeko.accountordermanagement.config;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastConfig {

    @Bean
    public HazelcastInstance hazelcastClient() {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClusterName("hazelcast-cluster");
        clientConfig.getNetworkConfig().addAddress("18.158.110.143:5701");

        return HazelcastClient.newHazelcastClient(clientConfig);
    }
}

