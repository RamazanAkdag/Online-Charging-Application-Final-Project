package org.example.controller;

import akka.actor.typed.ActorRef;
import com.hazelcast.core.HazelcastInstance;
import com.ramobeko.akka.Command;
import com.ramobeko.dgwtgf.model.UsageRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usage")
public class DGWUsageController {

    private static final Logger logger = LogManager.getLogger(DGWUsageController.class);

    private final HazelcastInstance hazelcastInstance;
    private final ActorRef<Command.UsageData> router;

    public DGWUsageController(
            @Qualifier("primaryHazelcastInstance") HazelcastInstance hazelcastInstance,
            ActorRef<Command.UsageData> router) {

        this.hazelcastInstance = hazelcastInstance;
        this.router = router;
        logger.info("DGWUsageController initialized with HazelcastInstance and Actor Router.");
    }

    @PostMapping
    public ResponseEntity<String> usage(@RequestBody UsageRequest usageRequest) {
        try {
            logger.info("📩 Received usage request: {}", usageRequest);

            Command.UsageData usageData = new Command.UsageData(
                    usageRequest.getUsageType(),
                    usageRequest.getUsageAmount(),
                    usageRequest.getSenderSubscNumber(),
                    usageRequest.getReceiverSubscNumber(),
                    usageRequest.getUsageTime()
            );

            logger.info("📤 Sending message to router: {}", usageData);
            router.tell(usageData);

            return ResponseEntity.ok("Usage data message sent to DGW actor");
        } catch (Exception e) {
            logger.error("Error processing usage request", e);
            throw new RuntimeException("Error processing usage request", e);
        }
    }

}
