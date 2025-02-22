package org.example;

import java.util.Random;

// Trafik üretici sınıf
public class TrafficGenerator {
    private static final String[] USAGE_TYPES = {"SMS", "CALL", "DATA"};
    private final Random random = new Random();

    public SubscriberService.UsageData generateUsageData() {
        String userId = "User-" + random.nextInt(1000);
        String usageType = USAGE_TYPES[random.nextInt(USAGE_TYPES.length)];
        int amount = random.nextInt(500);
        return new SubscriberService.UsageData(userId, usageType, amount);
    }
}