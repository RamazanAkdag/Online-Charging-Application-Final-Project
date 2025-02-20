package com.ramobeko.accountordermanagement.service.abstrct.hazelcast;


import java.util.Optional;

public interface IHazelcastService<K, V> {
    void save(K key, V value);
    Optional<V> get(K key);
    void remove(K key);
    boolean containsKey(K key);
}

