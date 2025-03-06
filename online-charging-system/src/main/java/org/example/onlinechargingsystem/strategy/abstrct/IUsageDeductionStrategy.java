package org.example.onlinechargingsystem.strategy.abstrct;

import com.ramobeko.akka.Command;
import com.ramobeko.ignite.IgniteSubscriber;
import org.example.onlinechargingsystem.repository.ignite.IgniteSubscriberRepository;

public interface IUsageDeductionStrategy {
    void deductBalance(IgniteSubscriber subscriber, int amount, IgniteSubscriberRepository igniteSubscriberRepository, Long subscNumber);
}
