package org.example.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.config.HazelcastClientManager;
import org.example.service.abstrct.ITrafficGeneratorService;
import org.example.service.abstrct.ISubscriberService;



public class ApplicationInitializer {

    private static final Logger logger = LogManager.getLogger(ApplicationInitializer.class);
    private final ISubscriberService subscriberService;
    private final ITrafficGeneratorService trafficGeneratorService;
    private final HazelcastClientManager clientManager;

    public ApplicationInitializer(
            HazelcastClientManager clientManager,
            ISubscriberService subscriberService,
            ITrafficGeneratorService trafficGeneratorService) {

        this.clientManager = clientManager;
        this.subscriberService = subscriberService;
        this.trafficGeneratorService = trafficGeneratorService;
    }

    public void run() {
        logger.info("Application starting: Fetching subscribers from Hazelcast and generating traffic.");
        try {
            while (true) {
                logger.debug("Fetching and printing subscriber list.");
                subscriberService.printSubscribers();

                logger.info("Generating and sending usage data for all subscribers.");
                trafficGeneratorService.generateAndSendUsageDataForAllSubscribers();

                logger.debug("Sleeping for 5 seconds.");
                Thread.sleep(5000);
            }
        } catch (InterruptedException e) {
            logger.error("Loop interrupted: {}", e.getMessage(), e); // Log exception details
            Thread.currentThread().interrupt(); // Correctly handle interruption
        } catch(Exception e){
            logger.error("An unexpected error occurred: {}", e.getMessage(), e);
        }
        finally {
            logger.info("Shutting down Hazelcast client.");
            clientManager.shutdown();
            logger.info("Application terminated.");
        }
    }
}
