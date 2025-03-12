package org.example.config;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.logging.Level;


public class HazelcastClientManager {

    private static final Logger logger = LogManager.getLogger(HazelcastClientManager.class);
    private final HazelcastInstance hazelcastClient;
    private final String subscriberCacheName;

    static {
        // Hazelcast loglarını INFO yerine WARNING seviyesine çek
        java.util.logging.Logger hazelcastLogger = java.util.logging.Logger.getLogger("com.hazelcast");
        hazelcastLogger.setLevel(java.util.logging.Level.WARNING);
    }

    public HazelcastClientManager(String... addresses) {
        ConfigLoader configLoader = new ConfigLoader();
        String clusterName = configLoader.getProperty("hazelcast.cluster.name");
        if (clusterName == null || clusterName.isEmpty()) {
            throw new IllegalStateException("Hazelcast cluster name is not configured in config.properties.");
        }

        subscriberCacheName = configLoader.getProperty("hazelcast.subscriber.cache.name");
        if (subscriberCacheName == null || subscriberCacheName.isEmpty()) {
            throw new IllegalStateException("Hazelcast subscriber cache name is not configured in config.properties.");
        }

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
        logger.debug("Retrieving {} IMap.", subscriberCacheName);
        try {
            IMap<String, Long> subscriberMap = hazelcastClient.getMap(subscriberCacheName);
            logger.debug("{} IMap retrieved successfully.", subscriberCacheName);
            return subscriberMap;
        } catch (Exception e) {
            logger.error("Error retrieving {} IMap: {}", subscriberCacheName, e.getMessage(), e);
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
