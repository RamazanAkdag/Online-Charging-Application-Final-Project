package com.ramobeko.accountordermanagement.repository.ignite;

import com.ramobeko.accountordermanagement.model.entity.Customer;
import org.apache.ignite.springdata.repository.IgniteRepository;
import org.apache.ignite.springdata.repository.config.RepositoryConfig;
import org.springframework.stereotype.Repository;

@RepositoryConfig(cacheName = "CustomerCache")
@Repository
public interface IgniteCustomerRepository extends IgniteRepository<Customer,Long> {
}
