package org.example;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;

public class HazelcastConfig {

    public static HazelcastInstance createHazelcastClient() {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClusterName("hazelcast-cluster");

        clientConfig.getNetworkConfig().addAddress("127.0.0.1:5701", "127.0.0.1:5702");

        return HazelcastClient.newHazelcastClient(clientConfig);
    }
}

