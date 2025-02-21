package com.ramobeko.accountordermanagement.service.concrete.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.ramobeko.accountordermanagement.service.abstrct.hazelcast.IHazelcastService;
import org.springframework.stereotype.Service;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class HazelcastSubscriberService implements IHazelcastService<String, Long> {

    private static final Logger logger = LoggerFactory.getLogger(HazelcastSubscriberService.class);
    private final HazelcastInstance hazelcastInstance;
    private static final String CACHE_NAME = "subscriberCache";

    public HazelcastSubscriberService(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @Override
    public void save(String phoneNumber, Long subscriberId) {
        logger.info("Saving subscriber with phone number: {} and ID: {} to Hazelcast cache", phoneNumber, subscriberId);
        IMap<String, Long> cache = hazelcastInstance.getMap(CACHE_NAME);
        cache.put(phoneNumber, subscriberId);
        logger.info("Subscriber with phone number: {} saved successfully", phoneNumber);
    }

    @Override
    public Optional<Long> get(String phoneNumber) {
        logger.info("Fetching subscriber ID with phone number: {} from Hazelcast cache", phoneNumber);
        IMap<String, Long> cache = hazelcastInstance.getMap(CACHE_NAME);
        return Optional.ofNullable(cache.get(phoneNumber));
    }

    @Override
    public void remove(String phoneNumber) {
        logger.info("Removing subscriber with phone number: {} from Hazelcast cache", phoneNumber);
        IMap<String, Long> cache = hazelcastInstance.getMap(CACHE_NAME);
        cache.remove(phoneNumber);
        logger.info("Subscriber with phone number: {} removed successfully", phoneNumber);
    }

    @Override
    public boolean containsKey(String phoneNumber) {
        IMap<String, Long> cache = hazelcastInstance.getMap(CACHE_NAME);
        boolean exists = cache.containsKey(phoneNumber);
        logger.info("Subscriber with phone number: {} exists in cache: {}", phoneNumber, exists);
        return exists;
    }
}