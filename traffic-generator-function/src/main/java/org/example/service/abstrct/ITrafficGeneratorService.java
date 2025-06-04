package org.example.service.abstrct;

import com.ramobeko.dgwtgf.model.UsageType;

/**
 * Interface for generating and sending traffic (usage data) for subscribers.
 * This service is responsible for simulating telecom usage events
 * and sending them to the system for processing.
 */
public interface ITrafficGeneratorService {

    /**
     * Generates and sends usage data for all subscribers in the system.
     * <p>
     * This method retrieves all available subscribers and generates usage
     * data randomly for each subscriber. The generated data is then sent
     * to the appropriate processing system.
     * </p>
     */
    void generateAndSendUsageDataForAllSubscribers();

    /**
     * Generates and sends usage data for a specific subscriber.
     * <p>
     * This method allows generating usage data for a single subscriber.
     * The user can specify the subscriber's phone number, the type of usage
     * (such as minutes, SMS, or data), and the amount of usage.
     * If no values are provided, random values will be generated.
     * </p>
     *
     * @param subscNumber The phone number (subscriber ID) of the user.
     * @param usageType   The type of usage (e.g., MINUTE, SMS, DATA).
     * @param amount      The amount of usage (e.g., minutes, MBs, or number of SMS).
     */
    void generateAndSendUsageDataForSubscriber(String subscNumber, UsageType usageType, int amount);
}
