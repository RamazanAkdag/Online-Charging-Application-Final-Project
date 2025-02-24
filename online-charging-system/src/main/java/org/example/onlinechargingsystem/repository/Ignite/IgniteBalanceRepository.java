package org.example.onlinechargingsystem.repository.Ignite;


import org.example.onlinechargingsystem.model.entity.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.apache.ignite.springdata.repository.IgniteRepository;
import org.apache.ignite.springdata.repository.config.RepositoryConfig;
import org.example.onlinechargingsystem.model.entity.Balance;

import java.util.Optional;

public interface IgniteBalanceRepository extends IgniteRepository<Balance, Long> {
    Optional<Balance> findBySubscriberId(Long subscriberId); // Eğer subscriberId eklediysek
}




