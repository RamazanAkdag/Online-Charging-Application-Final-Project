package com.ramobeko.accountordermanagement;

import com.ramobeko.accountordermanagement.model.entity.hazelcast.HazelcastCustomer;
import com.ramobeko.accountordermanagement.service.abstrct.hazelcast.IHazelcastService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class HazelcastTest implements CommandLineRunner {

    private final IHazelcastService<Long, HazelcastCustomer> customerService;

    public HazelcastTest(IHazelcastService<Long, HazelcastCustomer> customerService) {
        this.customerService = customerService;
    }

    @Override
    public void run(String... args) {
        HazelcastCustomer customer = new HazelcastCustomer(1L, "Ramazan Beko", "ramazan@example.com", "USER", new Date(), "Antalya, TR", "ACTIVE");
        customerService.save(customer.getId(), customer);

        customerService.get(1L)
                .ifPresent(c -> System.out.println("Cache'den gelen müşteri: " + c.getName()));
    }
}

