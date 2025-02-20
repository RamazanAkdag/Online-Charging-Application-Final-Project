package com.ramobeko.accountordermanagement.service.concrete.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.ramobeko.accountordermanagement.model.entity.hazelcast.HazelcastCustomer;

import com.ramobeko.accountordermanagement.service.abstrct.hazelcast.IHazelcastService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HazelcastCustomerService implements IHazelcastService<Long, HazelcastCustomer> {

    private final HazelcastInstance hazelcastInstance;
    private static final String CACHE_NAME = "customerCache";

    public HazelcastCustomerService(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @Override
    public void save(Long key, HazelcastCustomer value) {
        IMap<Long, HazelcastCustomer> cache = hazelcastInstance.getMap(CACHE_NAME);
        cache.put(key, value);
    }

    @Override
    public Optional<HazelcastCustomer> get(Long key) {
        IMap<Long, HazelcastCustomer> cache = hazelcastInstance.getMap(CACHE_NAME);
        return Optional.ofNullable(cache.get(key));
    }

    @Override
    public void remove(Long key) {
        IMap<Long, HazelcastCustomer> cache = hazelcastInstance.getMap(CACHE_NAME);
        cache.remove(key);
    }

    @Override
    public boolean containsKey(Long key) {
        IMap<Long, HazelcastCustomer> cache = hazelcastInstance.getMap(CACHE_NAME);
        return cache.containsKey(key);
    }
}
