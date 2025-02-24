package org.example.onlinechargingsystem.repository.Ignite;


import org.example.onlinechargingsystem.model.entity.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.apache.ignite.springdata.repository.IgniteRepository;

import org.apache.ignite.springdata.repository.config.RepositoryConfig;
import org.apache.ignite.IgniteCache;

@Repository
@RepositoryConfig(cacheName = "BalanceCache") // Ignite cache adını burada tanımlıyoruz
public interface IgniteBalanceRepository extends IgniteRepository<Balance, Long> {
    Optional<Balance> findBySubscriberId(Long subscriberId);

    IgniteCache<Long, Balance> cache(); // Cache metodunu elle tanımla
}





