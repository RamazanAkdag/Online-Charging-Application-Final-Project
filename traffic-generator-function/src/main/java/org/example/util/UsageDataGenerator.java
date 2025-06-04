package org.example.util;

import com.ramobeko.dgwtgf.model.UsageRequest;
import com.ramobeko.dgwtgf.model.UsageType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.repository.abstrct.SubscriberRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class UsageDataGenerator {

    private static final Logger logger = LogManager.getLogger(UsageDataGenerator.class);
    private final Random random = new Random();
    private final SubscriberRepository subscriberRepository;

    public UsageDataGenerator(SubscriberRepository subscriberRepository) {
        this.subscriberRepository = subscriberRepository;
        logger.info("UsageDataGenerator initialized.");
    }

    public UsageRequest generateUsageData(String subscNumber) {
        logger.debug("Generating usage data for subscriber: {}", subscNumber);
        UsageType usageType;
        int randNumber = this.random.nextInt(3);
        double usageAmount;
        switch (randNumber) {
            case 0:
                usageType = UsageType.DATA;
                usageAmount = random.nextDouble() * 1024;
                logger.debug("Usage type: DATA, amount: {}", usageAmount);
                break;
            case 1:
                usageType = UsageType.MINUTE;
                usageAmount = random.nextInt(180);
                logger.debug("Usage type: MINUTE, amount: {}", usageAmount);
                break;
            case 2:
                usageType = UsageType.SMS;
                usageAmount = 1;
                logger.debug("Usage type: SMS, amount: {}", usageAmount);
                break;
            default:
                usageType = UsageType.DATA;
                usageAmount = random.nextDouble() * 1024;
                logger.debug("Usage type: DATA (default), amount: {}", usageAmount);
                break;
        }

        Map<String, Long> subscribers = subscriberRepository.getSubscribers();
        String receiverSubscNumber = null;

        if (usageType == UsageType.MINUTE || usageType == UsageType.SMS) {
            if (!subscribers.isEmpty()) {
                List<String> keys = new ArrayList<>(subscribers.keySet());
                keys.remove(subscNumber);

                if (!keys.isEmpty()) {
                    receiverSubscNumber = keys.get(random.nextInt(keys.size()));
                    logger.debug("Receiver subscriber: {}", receiverSubscNumber);
                } else {
                    logger.debug("No other subscribers available for MINUTE/SMS.");
                }
            } else {
                logger.debug("Subscriber list is empty.");
            }
        }

        UsageRequest usageRequest = new UsageRequest();
        usageRequest.setUsageType(usageType);
        usageRequest.setUsageAmount(usageAmount);
        usageRequest.setSenderSubscNumber(subscNumber);
        usageRequest.setReceiverSubscNumber(receiverSubscNumber);
        usageRequest.setUsageTime(new Date());
        logger.debug("Generated UsageRequest: {}", usageRequest);

        return usageRequest;
    }
}