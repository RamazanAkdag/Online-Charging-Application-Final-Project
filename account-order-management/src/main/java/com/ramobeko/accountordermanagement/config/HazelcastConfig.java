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
        clientConfig.setClusterName("hazelcast-cluster"); // Küme adı
        clientConfig.getNetworkConfig().addAddress("127.0.0.1:5701", "127.0.0.1:5702"); // Düğümler

        return HazelcastClient.newHazelcastClient(clientConfig);
    }
}
