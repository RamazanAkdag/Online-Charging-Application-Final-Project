package com.ramobeko.accountordermanagement.controller;

import com.ramobeko.accountordermanagement.model.dto.*;
import com.ramobeko.accountordermanagement.model.dto.request.AuthRequest;
import com.ramobeko.accountordermanagement.model.dto.request.ChangePasswordRequest;
import com.ramobeko.accountordermanagement.model.dto.response.ApiResponse;
import com.ramobeko.accountordermanagement.model.dto.response.AuthResponse;
import com.ramobeko.accountordermanagement.model.entity.hazelcast.HazelcastSubscriber;
import com.ramobeko.accountordermanagement.model.entity.ignite.IgniteCustomer;
import com.ramobeko.accountordermanagement.model.entity.oracle.OracleCustomer;
import com.ramobeko.accountordermanagement.service.abstrct.hazelcast.IHazelcastService;
import com.ramobeko.accountordermanagement.service.abstrct.ignite.IIgniteCustomerService;
import com.ramobeko.accountordermanagement.service.abstrct.oracle.IOracleCustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final IOracleCustomerService oracleCustomerService;
    private final IIgniteCustomerService igniteCustomerService;
    private final IHazelcastService<Long, HazelcastSubscriber> hazelcastService;

    public AuthController(IOracleCustomerService oracleCustomerService,
                          IIgniteCustomerService igniteCustomerService,
                          IHazelcastService<Long, HazelcastSubscriber> hazelcastService) {
        this.oracleCustomerService = oracleCustomerService;
        this.igniteCustomerService = igniteCustomerService;
        this.hazelcastService = hazelcastService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(@RequestBody RegisterRequest request) {
        logger.info("Received register request for email: {} in Oracle DB", request.getEmail());

        OracleCustomer customer = oracleCustomerService.register(request);
        logger.info("Customer registered in Oracle DB with email: {}", customer.getEmail());

        IgniteCustomer igniteCustomer = new IgniteCustomer();
        igniteCustomer.setId(customer.getId());
        igniteCustomer.setName(customer.getName());
        igniteCustomer.setRole(customer.getRole());
        igniteCustomer.setEmail(customer.getEmail());
        igniteCustomer.setStartDate(customer.getStartDate());
        igniteCustomer.setStatus(customer.getStatus());
        igniteCustomer.setAddress(customer.getAddress());

        igniteCustomerService.register(igniteCustomer);
        logger.info("Customer registered successfully in both Oracle and Ignite DB: {}", customer.getEmail());

        HazelcastSubscriber hazelcastSubscriber = new HazelcastSubscriber(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getRole().toString(),
                customer.getStartDate(),
                customer.getAddress(),
                customer.getStatus()
        );
        hazelcastService.save(hazelcastSubscriber.getId(), hazelcastSubscriber);
        logger.info("Customer registered in Hazelcast cache: {}", customer.getEmail());

        return ResponseEntity.ok(new ApiResponse("Customer registered successfully!"));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateCustomer(@RequestBody AuthRequest request) {
        logger.info("Received authentication request for email: {} in Oracle DB", request.getEmail());
        String token = oracleCustomerService.authenticateCustomer(request);
        logger.info("Authentication successful for email: {}. Token generated.", request.getEmail());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        logger.info("Received change password request for email: {} in Oracle DB", request.getEmail());
        oracleCustomerService.changePassword(request);
        logger.info("Password changed successfully for email: {} in Oracle DB", request.getEmail());
        return ResponseEntity.ok(new ApiResponse("Password changed successfully!"));
    }
}
