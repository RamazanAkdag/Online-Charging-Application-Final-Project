package com.ramobeko.accountordermanagement.controller;

import com.ramobeko.accountordermanagement.model.dto.request.SubscriberRequest;
import com.ramobeko.accountordermanagement.model.dto.request.SubscriberUpdateRequest;
import com.ramobeko.accountordermanagement.model.dto.response.ApiResponse;
import com.ramobeko.accountordermanagement.model.entity.hazelcast.HazelcastSubscriber;
import com.ramobeko.accountordermanagement.model.shared.OracleSubscriber;
import com.ramobeko.accountordermanagement.service.abstrct.hazelcast.IHazelcastService;
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
    private final IOracleSubscriberService subscriberService;
    private final IHazelcastService<String, HazelcastSubscriber> hazelcastService;

    public SubscriberController(IOracleSubscriberService subscriberService,
                                IHazelcastService<String, HazelcastSubscriber> hazelcastService) {
        this.subscriberService = subscriberService;
        this.hazelcastService = hazelcastService;
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
     * Create a new subscriber (ID from JWT Token).
     */
    @PostMapping
    public ResponseEntity<?> createSubscriber(HttpServletRequest request, @RequestBody SubscriberRequest subscriberRequest) {
        Long userId = (Long) request.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(401).body(new ApiResponse("Unauthorized: User ID is missing in token."));
        }

        logger.info("Creating new subscriber for userId: {}", userId);

        // Veritabanına kullanıcıyı ekle
        subscriberService.create(userId, subscriberRequest);

        // Kullanıcıyı Oracle'dan tekrar çek
        OracleSubscriber createdSubscriber = subscriberService.readById(userId);

        if (createdSubscriber == null) {
            logger.error("Failed to retrieve created subscriber from Oracle DB for userId: {}", userId);
            return ResponseEntity.status(500).body(new ApiResponse("Failed to retrieve created subscriber."));
        }

        // HazelcastSubscriber nesnesini oluştur
        HazelcastSubscriber hazelcastSubscriber = new HazelcastSubscriber(
                createdSubscriber.getId(),
                createdSubscriber.getCustomer() != null ? createdSubscriber.getCustomer().getId() : null,
                createdSubscriber.getPackagePlan() != null ? createdSubscriber.getPackagePlan().getId() : null,
                createdSubscriber.getPhoneNumber(),
                createdSubscriber.getStartDate(),
                createdSubscriber.getEndDate(),
                createdSubscriber.getStatus()
        );

        // Hazelcast'e ekle (Telefon numarasını key olarak kullan)
        hazelcastService.save(hazelcastSubscriber.getPhoneNumber(), hazelcastSubscriber);
        logger.info("Subscriber stored in Hazelcast with phone: {}", createdSubscriber.getPhoneNumber());

        return ResponseEntity.ok(new ApiResponse("Subscriber Created Successfully for userId: " + userId));
    }



    /**
     * Update an existing subscriber.
     */
    @PutMapping("/update")
    public ResponseEntity<OracleSubscriber> updateSubscriber(@RequestBody SubscriberUpdateRequest request) {
        logger.info("Updating subscriber with ID: {}", request.getSubscriberId());
        subscriberService.update(request);
        return ResponseEntity.ok((OracleSubscriber) subscriberService.readById(request.getSubscriberId()));
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
