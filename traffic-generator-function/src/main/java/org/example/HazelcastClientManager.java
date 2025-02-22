package org.example;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

public class HazelcastClientManager {
    private final HazelcastInstance client;

    public HazelcastClientManager(String clusterName, String... memberAddresses) {
        Config config = new Config();
        config.setClusterName("hazelcast-cluster");
        config.getJetConfig().setEnabled(true);


        NetworkConfig networkConfig = config.getNetworkConfig();
        JoinConfig joinConfig = networkConfig.getJoin();
        joinConfig.getMulticastConfig().setEnabled(false);
        joinConfig.getTcpIpConfig().setEnabled(true)
                .addMember("127.0.0.1:5701")
                .addMember("127.0.0.1:5702");


        // Hazelcast Client başlatılıyor
        this.client = Hazelcast.newHazelcastInstance(config);
    }

    public IMap<String, Long> getSubscriberMap() {
        return client.getMap("subscriberCache");
    }

    public void shutdown() {
        client.shutdown();
    }
}
