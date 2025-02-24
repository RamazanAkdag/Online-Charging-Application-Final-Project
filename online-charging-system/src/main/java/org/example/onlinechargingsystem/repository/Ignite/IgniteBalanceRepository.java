package org.example.onlinechargingsystem.repository.Ignite;


import org.example.onlinechargingsystem.model.entity.Balance;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.apache.ignite.springdata.repository.IgniteRepository;

import org.apache.ignite.springdata.repository.config.RepositoryConfig;

@Repository
@RepositoryConfig(cacheName = "BalanceCache") // Ignite cache adı tanımlandı
public interface IgniteBalanceRepository extends IgniteRepository<Balance, Long> {
    Optional<Balance> findByBalSubscId(Long balSubscId); // subscriberId yerine doğru alan kullanıldı
}






