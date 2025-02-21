package com.ramobeko.accountordermanagement.controller;

import com.ramobeko.accountordermanagement.model.dto.request.SubscriberRequest;
import com.ramobeko.accountordermanagement.model.dto.request.SubscriberUpdateRequest;
import com.ramobeko.accountordermanagement.model.dto.response.ApiResponse;
import com.ramobeko.accountordermanagement.model.entity.hazelcast.HazelcastSubscriber;
import com.ramobeko.accountordermanagement.model.entity.ignite.IgniteSubscriber;
import com.ramobeko.accountordermanagement.model.shared.OracleSubscriber;
import com.ramobeko.accountordermanagement.service.abstrct.hazelcast.IHazelcastService;
import com.ramobeko.accountordermanagement.service.abstrct.ignite.IIgniteSubscriberService;
import com.ramobeko.accountordermanagement.service.abstrct.oracle.IOracleSubscriberService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriber")
public class SubscriberController {

    private static final Logger logger = LogManager.getLogger(SubscriberController.class);

    private final IOracleSubscriberService oracleSubscriberService;
    private final IHazelcastService<String, HazelcastSubscriber> hazelcastService;
    private final IIgniteSubscriberService igniteSubscriberService;

    public SubscriberController(IOracleSubscriberService oracleSubscriberService,
                                IHazelcastService<String, HazelcastSubscriber> hazelcastService,
                                IIgniteSubscriberService igniteSubscriberService) {
        this.oracleSubscriberService = oracleSubscriberService;
        this.hazelcastService = hazelcastService;
        this.igniteSubscriberService = igniteSubscriberService;
    }

    /**
     * Creates a new subscriber, storing it in Oracle, Ignite, and Hazelcast.
     */
    @PostMapping
    public ResponseEntity<?> createSubscriber(HttpServletRequest request, @RequestBody SubscriberRequest subscriberRequest) {
        Long userId = (Long) request.getAttribute("userId");

        if (userId == null) {
            logger.error("Unauthorized request: User ID is missing in token.");
            return ResponseEntity.status(401).body(new ApiResponse("Unauthorized: User ID is missing in token."));
        }

        logger.info("Creating new subscriber for userId: {}", userId);
        OracleSubscriber createdSubscriber = oracleSubscriberService.createSubscriber(userId, subscriberRequest);

        if (createdSubscriber == null) {
            logger.error("Failed to retrieve created subscriber from Oracle for userId: {}", userId);
            return ResponseEntity.status(500).body(new ApiResponse("Failed to retrieve created subscriber."));
        }

        // Store in Hazelcast
        HazelcastSubscriber hazelcastSubscriber = new HazelcastSubscriber(
                createdSubscriber.getId(),
                createdSubscriber.getCustomer() != null ? createdSubscriber.getCustomer().getId() : null,
                createdSubscriber.getPackagePlan() != null ? createdSubscriber.getPackagePlan().getId() : null,
                createdSubscriber.getPhoneNumber(),
                createdSubscriber.getStartDate(),
                createdSubscriber.getEndDate(),
                createdSubscriber.getStatus()
        );
        hazelcastService.save(hazelcastSubscriber.getPhoneNumber(), hazelcastSubscriber);
        logger.info("Subscriber stored in Hazelcast with phone: {}", createdSubscriber.getPhoneNumber());

        // Store in Ignite
        igniteSubscriberService.createFromOracle(createdSubscriber);
        logger.info("Subscriber stored in Ignite with ID: {}", createdSubscriber.getId());

        return ResponseEntity.ok(new ApiResponse("Subscriber created successfully for userId: " + userId));
    }

    /**
     * Updates an existing subscriber and synchronizes changes across Oracle, Ignite, and Hazelcast.
     */
    @PutMapping("/update")
    public ResponseEntity<OracleSubscriber> updateSubscriber(@RequestBody SubscriberUpdateRequest request) {
        logger.info("Updating subscriber with ID: {}", request.getSubscriberId());

        oracleSubscriberService.update(request);

        OracleSubscriber updatedSubscriber = oracleSubscriberService.readById(request.getSubscriberId());
        if (updatedSubscriber == null) {
            logger.error("Failed to retrieve updated subscriber from Oracle for ID: {}", request.getSubscriberId());
            return ResponseEntity.status(500).body(null);
        }

        // Update Hazelcast
        HazelcastSubscriber hazelcastSubscriber = new HazelcastSubscriber(
                updatedSubscriber.getId(),
                updatedSubscriber.getCustomer() != null ? updatedSubscriber.getCustomer().getId() : null,
                updatedSubscriber.getPackagePlan() != null ? updatedSubscriber.getPackagePlan().getId() : null,
                updatedSubscriber.getPhoneNumber(),
                updatedSubscriber.getStartDate(),
                updatedSubscriber.getEndDate(),
                updatedSubscriber.getStatus()
        );
        hazelcastService.save(hazelcastSubscriber.getPhoneNumber(), hazelcastSubscriber);
        logger.info("Subscriber updated in Hazelcast with phone: {}", updatedSubscriber.getPhoneNumber());

        // Update Ignite
        igniteSubscriberService.updateFromOracle(updatedSubscriber);
        logger.info("Subscriber updated in Ignite with ID: {}", updatedSubscriber.getId());

        return ResponseEntity.ok(updatedSubscriber);
    }

    /**
     * Deletes a subscriber and ensures it is removed from Oracle, Ignite, and Hazelcast.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSubscriber(@PathVariable Long id) {
        logger.info("Deleting subscriber with ID: {}", id);

        oracleSubscriberService.delete(id);

        logger.info("Subscriber deleted from Hazelcast with ID: {}", id);

        igniteSubscriberService.delete(id);
        logger.info("Subscriber deleted from Ignite with ID: {}", id);

        return ResponseEntity.ok("Subscriber deleted successfully.");
    }

    /**
     * Retrieves all subscribers from Oracle.
     */
    @GetMapping
    public ResponseEntity<List<OracleSubscriber>> getAllSubscribers() {
        logger.info("Fetching all subscribers");
        List<OracleSubscriber> subscribers = oracleSubscriberService.readAll();
        return ResponseEntity.ok(subscribers);
    }

    /**
     * Retrieves a specific subscriber by ID from Oracle.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OracleSubscriber> getSubscriberById(@PathVariable Long id) {
        logger.info("Fetching subscriber with ID: {}", id);
        OracleSubscriber subscriber = oracleSubscriberService.readById(id);
        return ResponseEntity.ok(subscriber);
    }

    /**
     * Retrieves a subscriber by phone number from Ignite.
     */
    @GetMapping("/phone/{phoneNumber}")
    public ResponseEntity<IgniteSubscriber> getSubscriberByPhoneNumber(@PathVariable String phoneNumber) {
        logger.info("Fetching subscriber with phone number: {}", phoneNumber);
        IgniteSubscriber igniteSubscriber = igniteSubscriberService.getByPhoneNumber(phoneNumber);
        if (igniteSubscriber == null) {
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.ok(igniteSubscriber);
    }
}
