package org.example.repository.concrete;

import com.hazelcast.map.IMap;
import org.example.repository.abstrct.SubscriberRepository;

import java.util.Map;

import com.hazelcast.map.IMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class HazelcastSubscriberRepository implements SubscriberRepository {

    private static final Logger logger = LogManager.getLogger(HazelcastSubscriberRepository.class);
    private final IMap<String, Long> subscriberMap;

    public HazelcastSubscriberRepository(IMap<String, Long> subscriberMap) {
        this.subscriberMap = subscriberMap;
        logger.info("HazelcastSubscriberRepository initialized with IMap: {}", subscriberMap.getName());
    }

    @Override
    public Map<String, Long> getSubscribers() {
        logger.debug("Retrieving subscribers from Hazelcast IMap.");
        Map<String, Long> subscribers = subscriberMap;
        logger.debug("Subscribers retrieved: {}", subscribers);
        return subscribers;
    }

    @Override
    public boolean isEmpty() {
        logger.debug("Checking if subscriberMap is empty.");
        boolean isEmpty = subscriberMap.isEmpty();
        logger.debug("SubscriberMap is empty: {}", isEmpty);
        return isEmpty;
    }
}