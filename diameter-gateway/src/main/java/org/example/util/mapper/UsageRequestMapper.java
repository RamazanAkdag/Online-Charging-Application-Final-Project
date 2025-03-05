package org.example.util.mapper;

import com.ramobeko.akka.Command;
import com.ramobeko.dgwtgf.model.UsageRequest;

public class UsageRequestMapper {

    public static Command.UsageData mapToUsageData(UsageRequest usageRequest) {
        return new Command.UsageData(
                usageRequest.getUsageType(),
                usageRequest.getUsageAmount(),
                usageRequest.getSenderSubscNumber(),
                usageRequest.getReceiverSubscNumber(),
                usageRequest.getUsageTime()
        );
    }
}

