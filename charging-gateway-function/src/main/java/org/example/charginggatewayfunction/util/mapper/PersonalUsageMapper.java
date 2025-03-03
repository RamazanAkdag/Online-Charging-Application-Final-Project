package org.example.charginggatewayfunction.util.mapper;

import com.ramobeko.kafka.CGFKafkaMessage;
import org.example.charginggatewayfunction.model.oracle.PersonalUsage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneId;
import java.time.ZoneOffset;

public class PersonalUsageMapper {

    private static final Logger logger = LoggerFactory.getLogger(PersonalUsageMapper.class);

    private PersonalUsageMapper() {
        // private constructor -> new'lemeye gerek yok
    }

    public static PersonalUsage mapToPersonalUsage(CGFKafkaMessage message) {
        // 🚀 Emoji ile bilgi log
        logger.info("🚀 Mapping CGFKafkaMessage to PersonalUsage: {}", message);

        PersonalUsage usage = new PersonalUsage();

        usage.setUserId(parseLongOrNull(message.getSenderSubscNumber()));
        usage.setGiverId(parseLongOrNull(message.getReceiverSubscNumber()));

        // usageType -> enum (SMS, MINUTE, DATA)
        usage.setUsageType(message.getUsageType());

        // usageTime -> hem LocalDate hem de OffsetDateTime olarak kullanabiliriz
        if (message.getUsageTime() != null) {
            usage.setUsageDate(
                    message.getUsageTime().toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
            );

            usage.setUsageTimestamp(
                    message.getUsageTime().toInstant()
                            .atOffset(ZoneOffset.UTC)
            );
        }

        // usageAmount -> usageDuration
        usage.setUsageDuration((int) message.getUsageAmount());

        // 🎉 Başarılı dönüşüm logu
        logger.info("🎉 Successfully mapped CGFKafkaMessage to PersonalUsage entity");

        return usage;
    }

    private static Long parseLongOrNull(String value) {
        try {
            return Long.valueOf(value);
        } catch (NumberFormatException | NullPointerException e) {
            logger.warn("⚠️ Değer Long'a parse edilemedi: {}", value);
            return null;
        }
    }
}
