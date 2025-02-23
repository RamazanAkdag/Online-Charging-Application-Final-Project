package org.example;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

class HazelcastClientManager {
    private final HazelcastInstance hazelcastClient;

    public HazelcastClientManager(String clusterName, String... addresses) {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setProperty("hazelcast.logging.type", "slf4j");
        clientConfig.setProperty("hazelcast.logging.level", "FINEST");
        clientConfig.setProperty("hazelcast.discovery.public.ip.enabled", "true");
        clientConfig.setClusterName(clusterName);
        clientConfig.getNetworkConfig().addAddress(addresses);
        clientConfig.getNetworkConfig().setRedoOperation(true);
        this.hazelcastClient = HazelcastClient.newHazelcastClient(clientConfig);
    }

    public IMap<String, Long> getSubscriberMap() {
        return hazelcastClient.getMap("subscriberCache");
    }

    public void shutdown() {
        hazelcastClient.shutdown();
    }
}