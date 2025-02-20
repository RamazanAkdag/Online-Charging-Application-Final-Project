package com.ramobeko.accountordermanagement.service.concrete.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.ramobeko.accountordermanagement.model.entity.hazelcast.HazelcastSubscriber;
import com.ramobeko.accountordermanagement.service.abstrct.hazelcast.IHazelcastService;
import org.springframework.stereotype.Service;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class HazelcastSubscriberService implements IHazelcastService<Long, HazelcastSubscriber> {

    private static final Logger logger = LoggerFactory.getLogger(HazelcastSubscriberService.class);
    private final HazelcastInstance hazelcastInstance;
    private static final String CACHE_NAME = "subscriberCache";

    public HazelcastSubscriberService(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @Override
    public void save(Long key, HazelcastSubscriber value) {
        logger.info("Saving subscriber with ID: {} to Hazelcast cache", key);
        IMap<Long, HazelcastSubscriber> cache = hazelcastInstance.getMap(CACHE_NAME);
        cache.put(key, value);
        logger.info("Subscriber with ID: {} saved successfully", key);
    }

    @Override
    public Optional<HazelcastSubscriber> get(Long key) {
        logger.info("Fetching subscriber with ID: {} from Hazelcast cache", key);
        IMap<Long, HazelcastSubscriber> cache = hazelcastInstance.getMap(CACHE_NAME);
        return Optional.ofNullable(cache.get(key));
    }

    @Override
    public void remove(Long key) {
        logger.info("Removing subscriber with ID: {} from Hazelcast cache", key);
        IMap<Long, HazelcastSubscriber> cache = hazelcastInstance.getMap(CACHE_NAME);
        cache.remove(key);
        logger.info("Subscriber with ID: {} removed successfully", key);
    }

    @Override
    public boolean containsKey(Long key) {
        IMap<Long, HazelcastSubscriber> cache = hazelcastInstance.getMap(CACHE_NAME);
        boolean exists = cache.containsKey(key);
        logger.info("Subscriber with ID: {} exists in cache: {}", key, exists);
        return exists;
    }
}
