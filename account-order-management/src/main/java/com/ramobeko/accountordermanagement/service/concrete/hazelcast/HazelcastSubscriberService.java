package com.ramobeko.accountordermanagement.service.concrete.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.ramobeko.accountordermanagement.service.abstrct.hazelcast.IHazelcastService;
import org.springframework.stereotype.Service;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;

@Service
public class HazelcastSubscriberService implements IHazelcastService<String, Long> {

    private static final Logger logger = LoggerFactory.getLogger(HazelcastSubscriberService.class);
    private final HazelcastInstance hazelcastInstance;

    // application.properties dosyasından CACHE_NAME değerini alıyoruz
    @Value("${hazelcast.cache.name}")
    private String cacheName;

    public HazelcastSubscriberService(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @Override
    public void save(String phoneNumber, Long subscriberId) {
        logger.info("🔐 Saving subscriber with phone number: {} and ID: {} to Hazelcast cache 📥", phoneNumber, subscriberId);
        IMap<String, Long> cache = hazelcastInstance.getMap(cacheName);
        cache.put(phoneNumber, subscriberId);
        logger.info("✅ Subscriber with phone number: {} saved successfully 👍", phoneNumber);
    }

    @Override
    public Optional<Long> get(String phoneNumber) {
        logger.info("🔍 Fetching subscriber ID with phone number: {} from Hazelcast cache ⏳", phoneNumber);
        IMap<String, Long> cache = hazelcastInstance.getMap(cacheName);
        Long subscriberId = cache.get(phoneNumber);
        if (subscriberId != null) {
            logger.info("✅ Subscriber found with ID: {} for phone number: {}", subscriberId, phoneNumber);
        } else {
            logger.warn("⚠️ No subscriber found with phone number: {}", phoneNumber);
        }
        return Optional.ofNullable(subscriberId);
    }

    @Override
    public void remove(String phoneNumber) {
        logger.info("❌ Removing subscriber with phone number: {} from Hazelcast cache 🗑️", phoneNumber);
        IMap<String, Long> cache = hazelcastInstance.getMap(cacheName);
        cache.remove(phoneNumber);
        logger.info("✅ Subscriber with phone number: {} removed successfully 🧹", phoneNumber);
    }

    @Override
    public boolean containsKey(String phoneNumber) {
        IMap<String, Long> cache = hazelcastInstance.getMap(cacheName);
        boolean exists = cache.containsKey(phoneNumber);
        if (exists) {
            logger.info("🔑 Subscriber with phone number: {} exists in cache ✅", phoneNumber);
        } else {
            logger.warn("❌ Subscriber with phone number: {} does not exist in cache ⚠️", phoneNumber);
        }
        return exists;
    }
}
