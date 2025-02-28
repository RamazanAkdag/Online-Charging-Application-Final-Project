package org.example.service.concrete;


import org.example.repository.abstrct.SubscriberRepository;
import org.example.service.abstrct.ISubscriberService;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class SubscriberService implements ISubscriberService {

    private static final Logger logger = LogManager.getLogger(SubscriberService.class);
    private final SubscriberRepository subscriberRepository;

    public SubscriberService(SubscriberRepository subscriberRepository) {
        this.subscriberRepository = subscriberRepository;
        logger.info("SubscriberService initialized.");
    }

    @Override
    public void printSubscribers() {
        logger.info("Printing subscribers.");
        logger.debug("Retrieving subscriber map.");
        Map<String, Long> subscribers = subscriberRepository.getSubscribers();

        if (subscribers.isEmpty()) {
            logger.warn("No subscribers found in Hazelcast.");
        } else {
            logger.info("Subscribers found: {}", subscribers);
            logger.info("----- Güncel Aboneler -----");
            for (Map.Entry<String, Long> entry : subscribers.entrySet()) {
                logger.info("Telefon: {} -> ID: {}", entry.getKey(), entry.getValue());
            }
        }
    }
}