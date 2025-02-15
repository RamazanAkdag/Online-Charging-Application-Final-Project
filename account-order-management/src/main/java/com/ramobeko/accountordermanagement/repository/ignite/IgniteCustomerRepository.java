package com.ramobeko.accountordermanagement.repository.ignite;

import com.ramobeko.accountordermanagement.model.entity.ignite.IgniteCustomer;
import com.ramobeko.accountordermanagement.model.entity.oracle.OracleCustomer;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.springdata.repository.IgniteRepository;
import org.apache.ignite.springdata.repository.config.RepositoryConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.cache.expiry.ExpiryPolicy;
import java.io.Serializable;

import org.apache.ignite.springdata.repository.IgniteRepository;
import org.apache.ignite.springdata.repository.config.RepositoryConfig;
import org.apache.ignite.IgniteCache;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryConfig(cacheName = "CustomerCache", autoCreateCache = true)
public interface IgniteCustomerRepository extends IgniteRepository<IgniteCustomer, Long> {

}
