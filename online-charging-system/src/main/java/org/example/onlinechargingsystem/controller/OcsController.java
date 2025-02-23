package org.example.onlinechargingsystem.controller;

import org.example.onlinechargingsystem.model.entity.UsageData;
import org.example.onlinechargingsystem.service.concrete.OcsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/ocs")
public class OcsController {
    private final OcsService ocsService;

    public OcsController(OcsService ocsService) {
        this.ocsService = ocsService;
    }

    @PostMapping("/usage")
    public ResponseEntity<String> processUsage(@RequestBody UsageData data) {
        boolean success = ocsService.processUsageData(data);
        return success
                ? ResponseEntity.ok("Usage data processed successfully")
                : ResponseEntity.badRequest().body("Insufficient balance or user not found");
    }
}

