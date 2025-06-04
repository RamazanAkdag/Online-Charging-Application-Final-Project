package com.ramobeko.accountordermanagement.repository.ignite;

import com.ramobeko.accountordermanagement.model.entity.ignite.IgniteCustomer;
import org.apache.ignite.springdata.repository.IgniteRepository;
import org.apache.ignite.springdata.repository.config.RepositoryConfig;
import org.springframework.stereotype.Repository;


@Repository
@RepositoryConfig(cacheName = "CustomerCache", autoCreateCache = true)
public interface IgniteCustomerRepository extends IgniteRepository<IgniteCustomer, Long> {

}
