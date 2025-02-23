package org.example.onlinechargingsystem.service.concrete;

import org.apache.ignite.IgniteCache;
import org.example.onlinechargingsystem.model.entity.UsageData;
import org.example.onlinechargingsystem.model.entity.UserBalance;
import org.example.onlinechargingsystem.repository.IgniteRepository;
import org.springframework.stereotype.Service;


@Service
public class OcsService {
    private final IgniteRepository igniteRepository;

    public OcsService(IgniteRepository igniteRepository) {
        this.igniteRepository = igniteRepository;
    }

    public boolean processUsageData(UsageData data) {
        IgniteCache<String, UserBalance> cache = igniteRepository.getCache();
        UserBalance userBalance = cache.get(data.getUserId());

        if (userBalance == null) {
            return false;
        }

        long cost = calculateCost(data);
        if (userBalance.getBalance() >= cost) {
            userBalance.setBalance(userBalance.getBalance() - cost);
            cache.put(data.getUserId(), userBalance);
            return true;
        } else {
            return false;
        }
    }

    private long calculateCost(UsageData data) {
        return switch (data.getServiceType()) {
            case "voice" -> data.getUsageAmount() * 2;
            case "sms" -> data.getUsageAmount();
            case "data" -> data.getUsageAmount() * 5;
            default -> 0;
        };
    }
}

