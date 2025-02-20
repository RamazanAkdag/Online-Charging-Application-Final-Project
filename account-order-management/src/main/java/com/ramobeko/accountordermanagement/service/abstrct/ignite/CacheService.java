package com.ramobeko.accountordermanagement.service.abstrct.ignite;

import java.util.Optional;

public interface CacheService<K, V> {
    void put(K key, V value);
    Optional<V> get(K key);
    void remove(K key);
    boolean containsKey(K key);
}

