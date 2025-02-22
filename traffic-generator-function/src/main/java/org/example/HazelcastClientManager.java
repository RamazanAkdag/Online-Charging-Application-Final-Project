package org.example;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

// Hazelcast bağlantısını yöneten sınıf
class HazelcastClientManager {
    private final HazelcastInstance hazelcastClient;

    public HazelcastClientManager(String clusterName, String... addresses) {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClusterName(clusterName);
        clientConfig.getNetworkConfig().addAddress(addresses);
        this.hazelcastClient = HazelcastClient.newHazelcastClient(clientConfig);
    }

    public IMap<String, Long> getSubscriberMap() {
        return hazelcastClient.getMap("subscriberCache");
    }
    public void shutdown() {
        hazelcastClient.shutdown();
    }
}
