package org.example.controller;

import com.ramobeko.dgwtgf.model.UsageRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.service.concrete.UsageService;

@RestController
@RequestMapping("/usage")
public class DGWUsageController {

    private static final Logger logger = LogManager.getLogger(DGWUsageController.class);
    private final UsageService usageService;

    public DGWUsageController(UsageService usageService) {
        this.usageService = usageService;
        logger.info("DGWUsageController initialized with UsageService.");
    }

    @PostMapping
    public ResponseEntity<String> usage(@RequestBody UsageRequest usageRequest) {
        logger.info("📩 Received usage request: {}", usageRequest);
        try {
            String response = usageService.processUsageRequest(usageRequest);
            if ("User does not exist".equals(response)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error processing usage request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing usage request");
        }
    }
}
