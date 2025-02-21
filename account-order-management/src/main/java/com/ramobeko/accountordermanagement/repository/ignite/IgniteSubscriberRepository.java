package com.ramobeko.accountordermanagement.repository.ignite;

import com.ramobeko.accountordermanagement.model.entity.ignite.IgniteCustomer;
import com.ramobeko.accountordermanagement.model.entity.ignite.IgniteSubscriber;
import org.apache.ignite.springdata.repository.IgniteRepository;
import org.apache.ignite.springdata.repository.config.RepositoryConfig;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryConfig(cacheName = "SubscriberCache", autoCreateCache = true)
public interface IgniteSubscriberRepository extends IgniteRepository<IgniteSubscriber, Long> {

    IgniteSubscriber findByPhoneNumber(String phoneNumber);
}
