package org.example.config;


import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;

import com.hazelcast.map.IMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HazelcastClientManager {

    private static final Logger logger = LogManager.getLogger(HazelcastClientManager.class);
    private final HazelcastInstance hazelcastClient;

    public HazelcastClientManager(String clusterName, String... addresses) {
        logger.info("Initializing HazelcastClientManager with clusterName: {} and addresses: {}", clusterName, addresses);
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClusterName(clusterName);
        clientConfig.getNetworkConfig().addAddress(addresses);
        try {
            this.hazelcastClient = HazelcastClient.newHazelcastClient(clientConfig);
            logger.info("Hazelcast client successfully created and connected.");
        } catch (Exception e) {
            logger.error("Error creating Hazelcast client: {}", e.getMessage(), e);
            throw e;
        }
    }

    public IMap<String, Long> getSubscriberMap() {
        logger.debug("Retrieving subscriberCache IMap.");
        try {
            IMap<String, Long> subscriberMap = hazelcastClient.getMap("subscriberCache");
            logger.debug("SubscriberCache IMap retrieved successfully.");
            return subscriberMap;
        } catch (Exception e) {
            logger.error("Error retrieving subscriberCache IMap: {}", e.getMessage(), e);
            throw e;
        }
    }

    public void shutdown() {
        logger.info("Shutting down Hazelcast client.");
        try {
            hazelcastClient.shutdown();
            logger.info("Hazelcast client shutdown successfully.");
        } catch (Exception e) {
            logger.error("Error shutting down Hazelcast client: {}", e.getMessage(), e);
        }
    }
}