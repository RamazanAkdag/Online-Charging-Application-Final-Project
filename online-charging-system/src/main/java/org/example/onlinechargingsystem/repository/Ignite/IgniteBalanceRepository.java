package org.example.onlinechargingsystem.repository.Ignite;

import org.apache.ignite.springdata.repository.IgniteRepository;
import org.apache.ignite.springdata.repository.config.RepositoryConfig;
import org.example.onlinechargingsystem.model.entity.Balance;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
@RepositoryConfig(cacheName = "BalanceCache", autoCreateCache = true)
public interface IgniteBalanceRepository extends IgniteRepository<Balance, Long> {
    Optional<Balance> findBySubscriberId(Long subscriberId); // 🔹 **Metot adı düzeltildi**
}
