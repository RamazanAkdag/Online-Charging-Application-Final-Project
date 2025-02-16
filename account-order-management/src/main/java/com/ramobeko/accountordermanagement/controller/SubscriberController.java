package com.ramobeko.accountordermanagement.controller;

import com.ramobeko.accountordermanagement.model.dto.request.SubscriberRequest;
import com.ramobeko.accountordermanagement.model.dto.response.ApiResponse;
import com.ramobeko.accountordermanagement.model.shared.OracleSubscriber;
import com.ramobeko.accountordermanagement.service.abstrct.oracle.IOracleSubscriberService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriber")
public class SubscriberController {

    private static final Logger logger = LogManager.getLogger(SubscriberController.class);
    private final IOracleSubscriberService subscriberService;

    public SubscriberController(IOracleSubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    /**
     * Get all subscribers.
     */
    @GetMapping
    public ResponseEntity<List<OracleSubscriber>> getAllSubscribers() {
        logger.info("Fetching all subscribers");
        List<OracleSubscriber> subscribers = subscriberService.readAll();
        return ResponseEntity.ok(subscribers);
    }

    /**
     * Get a specific subscriber by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OracleSubscriber> getSubscriberById(@PathVariable Long id) {
        logger.info("Fetching subscriber with ID: {}", id);
        OracleSubscriber subscriber = (OracleSubscriber) subscriberService.readById(id);
        return ResponseEntity.ok(subscriber);
    }

    /**
     * Create a new subscriber.
     */
    @PostMapping
    public ResponseEntity<?> createSubscriber(@RequestBody SubscriberRequest request) {
        logger.info("Creating new subscriber with phone number: {}", request.getPhoneNumber());
        subscriberService.create(request);
        return ResponseEntity.ok(new ApiResponse("Subscriber Created Successfully"));
    }

    /**
     * Update an existing subscriber.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OracleSubscriber> updateSubscriber(@PathVariable Long id, @RequestBody SubscriberRequest request) {
        logger.info("Updating subscriber with ID: {}", id);
        request.setCustomerId(id);
        subscriberService.update(request);
        return ResponseEntity.ok((OracleSubscriber) subscriberService.readById(id));
    }

    /**
     * Delete a subscriber by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSubscriber(@PathVariable Long id) {
        logger.info("Deleting subscriber with ID: {}", id);
        subscriberService.delete(id);
        return ResponseEntity.ok("Subscriber deleted successfully.");
    }
}
