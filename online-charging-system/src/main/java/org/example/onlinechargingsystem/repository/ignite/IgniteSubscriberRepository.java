package org.example.onlinechargingsystem.repository.ignite;

import com.ramobeko.ignite.IgniteSubscriber;
import org.apache.ignite.springdata.repository.IgniteRepository;
import org.apache.ignite.springdata.repository.config.RepositoryConfig;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryConfig(cacheName = "SubscriberCache", autoCreateCache = true)
public interface IgniteSubscriberRepository extends IgniteRepository<IgniteSubscriber, Long> {
    IgniteSubscriber findByPhoneNumber(String phoneNumber);



}