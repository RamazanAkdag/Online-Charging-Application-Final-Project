package org.example.service.concrete;

import com.ramobeko.akka.Command;
import com.ramobeko.dgwtgf.model.UsageRequest;
import com.ramobeko.dgwtgf.model.UsageType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.http.abstrct.TrafficSender;
import org.example.repository.abstrct.SubscriberRepository;
import org.example.service.abstrct.ITrafficGeneratorService;
import org.example.util.UsageDataGenerator;

import java.util.*;

public class TrafficGeneratorService implements ITrafficGeneratorService {

    private static final Logger logger = LogManager.getLogger(TrafficGeneratorService.class);
    private final SubscriberRepository subscriberRepository;
    private final TrafficSender trafficSender;
    private final UsageDataGenerator usageDataGenerator;

    public TrafficGeneratorService(SubscriberRepository subscriberRepository, UsageDataGenerator usageDataGenerator, TrafficSender trafficSender) {
        this.subscriberRepository = subscriberRepository;
        this.trafficSender = trafficSender;
        this.usageDataGenerator = usageDataGenerator;
        logger.info("TrafficGeneratorService initialized.");
    }

    @Override
    public void generateAndSendUsageDataForAllSubscribers() {
        logger.info("Generating and sending usage data for all subscribers.");
        if (subscriberRepository.isEmpty()) {
            logger.warn("No subscribers found in Hazelcast, traffic generation skipped.");
            return;
        }

        logger.info("Generating traffic data for all subscribers...");

        for (Map.Entry<String, Long> entry : subscriberRepository.getSubscribers().entrySet()) {
            String subscNumber = entry.getKey();
            UsageRequest usageData = usageDataGenerator.generateUsageData(subscNumber);
            logger.debug("Usage data generated -> {}", usageData);

            try {
                trafficSender.sendUsageData(usageData);
                logger.debug("Usage data sent successfully for subscriber: {}", subscNumber);
            } catch (Exception e) {
                logger.error("Error sending usage data for subscriber: {}", subscNumber, e);
            }
        }
        logger.info("Usage data generation and sending completed.");
    }

    @Override
    public void generateAndSendUsageDataForSubscriber(String subscNumber, UsageType usageType, int amount) {
        logger.info("Generating and sending usage data for subscriber: {}", subscNumber);

        if (!subscriberRepository.getSubscribers().containsKey(subscNumber)) {
            logger.warn("Subscriber {} not found in Hazelcast, skipping traffic generation.", subscNumber);
            return;
        }

        String receiverSubscNumber = getRandomReceiver(subscNumber);

        Date usageTime = new Date();

        UsageRequest usageData;
        if (usageType == null || amount == -1) {
            usageData = usageDataGenerator.generateUsageData(subscNumber);
        } else {
            usageData = new UsageRequest(usageType, amount, subscNumber, receiverSubscNumber, usageTime);
        }

        logger.debug("Usage data generated -> {}", usageData);

        try {
            trafficSender.sendUsageData(usageData);
            logger.debug("Usage data sent successfully for subscriber: {}", subscNumber);
        } catch (Exception e) {
            logger.error("Error sending usage data for subscriber: {}", subscNumber, e);
        }
    }

    /**
     * 🔹 **Veritabanından rastgele bir alıcı (receiver) seçer.**
     * 📌 **Gönderici ile aynı kişi olmamalıdır.**
     */
    private String getRandomReceiver(String senderSubscNumber) {
        List<String> subscribers = new ArrayList<>(subscriberRepository.getSubscribers().keySet());

        if (subscribers.size() <= 1) {
            logger.warn("Only one subscriber exists, setting receiver as sender itself.");
            return senderSubscNumber;
        }

        Random random = new Random();
        String receiverSubscNumber;

        do {
            receiverSubscNumber = subscribers.get(random.nextInt(subscribers.size()));
        } while (receiverSubscNumber.equals(senderSubscNumber));

        logger.debug("Selected random receiver: {}", receiverSubscNumber);
        return receiverSubscNumber;
    }

}