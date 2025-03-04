package org.example.onlinechargingsystem.service.concrete;

import com.ramobeko.dgwtgf.model.UsageType;
import com.ramobeko.ignite.IgniteSubscriber;
import com.ramobeko.ignite.IgniteBalance;
import com.ramobeko.kafka.message.ABMFKafkaMessage;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.onlinechargingsystem.repository.ignite.IgniteSubscriberRepository;
import org.example.onlinechargingsystem.service.abstrct.IBalanceService;
import org.example.onlinechargingsystem.service.abstrct.IKafkaProducerService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BalanceService implements IBalanceService {

    private static final Logger logger = LogManager.getLogger(BalanceService.class);

    private final IgniteSubscriberRepository igniteSubscriberRepository;
    private final IKafkaProducerService kafkaProducerService;

    private static final String KAFKA_TOPIC = "usage-events";

    @Override
    public IgniteBalance getBalance(Long subscNumber) {
        logger.info("🔍 Fetching balance for subscriber: {}", subscNumber);

        IgniteSubscriber subscriber = igniteSubscriberRepository.findById(subscNumber)
                .orElseThrow(() -> {
                    logger.error("❌ Subscriber not found in Ignite for subscNumber: {}", subscNumber);
                    return new RuntimeException("Subscriber not found");
                });

        logger.info("📜 Subscriber details: {}", subscriber); // Aboneyi logla

        if (subscriber.getBalances().isEmpty()) {
            logger.error("❌ No balance record found for subscriber: {}", subscNumber);
            throw new RuntimeException("Balance not found");
        }

        return subscriber.getBalances().get(0); // İlk kayıt alınıyor, gerekirse filtreleme yapılabilir
    }

    @Override
    public void deductBalanceForMinutes(Long subscNumber, int amount) {
        logger.info("📉 Deducting {} minutes from balance for subscNumber: {}", amount, subscNumber);

        IgniteSubscriber subscriber = igniteSubscriberRepository.findById(subscNumber)
                .orElseThrow(() -> {
                    logger.error("❌ Subscriber not found: {}", subscNumber);
                    return new RuntimeException("Subscriber not found");
                });

        logger.info("📜 Subscriber details before deduction: {}", subscriber); // Aboneyi logla

        IgniteBalance balance = getBalance(subscNumber);
        if (balance.getLevelMinutes() < amount) {
            logger.error("❌ Insufficient minutes balance for subscNumber: {}", subscNumber);
            throw new RuntimeException("Insufficient minutes balance");
        }

        balance.setLevelMinutes(balance.getLevelMinutes() - amount);
        igniteSubscriberRepository.save(subscNumber, subscriber);
        logger.info("✅ Updated minutes balance for subscNumber: {}", subscNumber);

        ABMFKafkaMessage message = new ABMFKafkaMessage(subscNumber, UsageType.MINUTE, amount);
        kafkaProducerService.sendUsageData(KAFKA_TOPIC, message);
        logger.info("📤 Kafka message sent: {}", message);
    }

    @Override
    public void deductBalanceForSms(Long subscNumber, int amount) {
        logger.info("📉 Deducting {} SMS from balance for subscNumber: {}", amount, subscNumber);

        IgniteSubscriber subscriber = igniteSubscriberRepository.findById(subscNumber)
                .orElseThrow(() -> {
                    logger.error("❌ Subscriber not found: {}", subscNumber);
                    return new RuntimeException("Subscriber not found");
                });

        logger.info("📜 Subscriber details before deduction: {}", subscriber); // Aboneyi logla

        IgniteBalance balance = getBalance(subscNumber);
        if (balance.getLevelSms() < amount) {
            logger.error("❌ Insufficient SMS balance for subscNumber: {}", subscNumber);
            throw new RuntimeException("Insufficient SMS balance");
        }

        balance.setLevelSms(balance.getLevelSms() - amount);
        igniteSubscriberRepository.save(subscNumber, subscriber);
        logger.info("✅ Updated SMS balance for subscNumber: {}", subscNumber);

        ABMFKafkaMessage message = new ABMFKafkaMessage(subscNumber, UsageType.SMS, amount);
        kafkaProducerService.sendUsageData(KAFKA_TOPIC, message);
        logger.info("📤 Kafka message sent: {}", message);
    }

    @Override
    public void deductBalanceForData(Long subscNumber, int amount) {
        logger.info("📉 Deducting {} MB data from balance for subscNumber: {}", amount, subscNumber);

        IgniteSubscriber subscriber = igniteSubscriberRepository.findById(subscNumber)
                .orElseThrow(() -> {
                    logger.error("❌ Subscriber not found: {}", subscNumber);
                    return new RuntimeException("Subscriber not found");
                });

        logger.info("📜 Subscriber details before deduction: {}", subscriber); // Aboneyi logla

        IgniteBalance balance = getBalance(subscNumber);
        if (balance.getLevelData() < amount) {
            logger.error("❌ Insufficient data balance for subscNumber: {}", subscNumber);
            throw new RuntimeException("Insufficient data balance");
        }

        balance.setLevelData(balance.getLevelData() - amount);
        igniteSubscriberRepository.save(subscNumber, subscriber);
        logger.info("✅ Updated data balance for subscNumber: {}", subscNumber);

        ABMFKafkaMessage message = new ABMFKafkaMessage(subscNumber, UsageType.DATA, amount);
        kafkaProducerService.sendUsageData(KAFKA_TOPIC, message);
        logger.info("📤 Kafka message sent: {}", message);
    }
}
