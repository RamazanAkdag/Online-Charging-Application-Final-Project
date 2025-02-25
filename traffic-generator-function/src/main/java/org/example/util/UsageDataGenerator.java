package org.example.util;

import com.ramobeko.akka.Command;
import java.util.List;
import java.util.Random;

public class UsageDataGenerator {
    private final List<String> usageTypes;
    private final Random random = new Random();

    public UsageDataGenerator(List<String> usageTypes) {
        this.usageTypes = usageTypes;
    }

    public Command.UsageData generateUsageData(String userId) {
        String usageType = usageTypes.get(random.nextInt(usageTypes.size()));
        int amount = random.nextInt(500);
        return new Command.UsageData(userId, usageType, amount);
    }
}
