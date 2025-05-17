package org.example.onlinechargingsystem.util.mapper;

import com.ramobeko.akka.Command;
import com.ramobeko.kafka.message.CGFKafkaMessage;

public class CGFKafkaMessageMapper {

    public static CGFKafkaMessage toKafkaMessage(Command.UsageData data) {
        return new CGFKafkaMessage(
                data.getUsageType(),
                data.getUsageAmount(),
                data.getSenderSubscNumber(),
                data.getReceiverSubscNumber(),
                data.getUsageTime()
        );
    }
}

