package com.ramobeko.accountordermanagement.config;


import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class HazelcastConfig {

    @Value("${hazelcast.cluster.members}")
    private String members;

    @Bean
    public HazelcastInstance hazelcastClientInstance() {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClusterName("hazelcast-cluster");

        // IP adreslerini virgüle göre bölerek listeye çeviriyoruz
        List<String> memberList = Arrays.asList(members.split(","));
        clientConfig.getNetworkConfig().addAddress(memberList.toArray(new String[0]));

        return HazelcastClient.newHazelcastClient(clientConfig);
    }
}

