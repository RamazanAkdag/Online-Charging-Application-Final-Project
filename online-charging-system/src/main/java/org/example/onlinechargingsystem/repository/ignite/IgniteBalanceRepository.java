package org.example.onlinechargingsystem.repository.ignite;

import org.apache.ignite.springdata.repository.IgniteRepository;
import org.apache.ignite.springdata.repository.config.RepositoryConfig;
import org.example.onlinechargingsystem.model.entity.Balance;
import org.springframework.stereotype.Repository;


@Repository
@RepositoryConfig(cacheName = "BalanceCache", autoCreateCache = true)
public interface IgniteBalanceRepository extends IgniteRepository<Balance, Long> {

}
