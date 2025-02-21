package com.ramobeko.accountordermanagement.service.abstrct.hazelcast;


import java.util.Optional;

public interface IHazelcastService<String, V> {
    void save(String key, V value);
    Optional<V> get(String key);
    void remove(String key);
    boolean containsKey(String key);
}


