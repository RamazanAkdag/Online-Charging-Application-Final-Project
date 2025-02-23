package org.example.onlinechargingsystem.repository;

package org.example.ocs.repository;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.configuration.CacheConfiguration;
import org.example.ocs.model.UserBalance;
import org.springframework.stereotype.Repository;

@Repository
public class IgniteRepository {
    private final Ignite ignite;

    public IgniteRepository(Ignite ignite) {
        this.ignite = ignite;
    }

    public IgniteCache<String, UserBalance> getCache() {
        return ignite.getOrCreateCache(new CacheConfiguration<>("userBalanceCache"));
    }
}

