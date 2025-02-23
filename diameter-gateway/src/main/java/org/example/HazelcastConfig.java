package org.example;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;

public class HazelcastConfig {

    public static HazelcastInstance createHazelcastClient() {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClusterName("hazelcast-cluster");
        clientConfig.setProperty("hazelcast.logging.type", "slf4j");
        clientConfig.setProperty("hazelcast.logging.level", "FINEST");

        // Uzak Hazelcast üyelerinin adreslerini ekle
        clientConfig.getNetworkConfig()
                .addAddress("18.158.110.143:5701", "18.158.110.143:5702");

        return HazelcastClient.newHazelcastClient(clientConfig);
    }
}
