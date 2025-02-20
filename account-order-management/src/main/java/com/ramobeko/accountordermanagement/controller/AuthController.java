package com.ramobeko.accountordermanagement.controller;

import com.ramobeko.accountordermanagement.model.dto.*;
import com.ramobeko.accountordermanagement.model.dto.request.AuthRequest;
import com.ramobeko.accountordermanagement.model.dto.request.ChangePasswordRequest;
import com.ramobeko.accountordermanagement.model.dto.response.ApiResponse;
import com.ramobeko.accountordermanagement.model.dto.response.AuthResponse;
import com.ramobeko.accountordermanagement.model.entity.hazelcast.HazelcastCustomer;
import com.ramobeko.accountordermanagement.model.entity.ignite.IgniteCustomer;
import com.ramobeko.accountordermanagement.model.entity.oracle.OracleCustomer;
import com.ramobeko.accountordermanagement.service.abstrct.hazelcast.IHazelcastService;
import com.ramobeko.accountordermanagement.service.abstrct.ignite.IIgniteCustomerService;
import com.ramobeko.accountordermanagement.service.abstrct.oracle.IOracleCustomerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LogManager.getLogger(AuthController.class);

    private final IOracleCustomerService oracleCustomerService;
    private final IIgniteCustomerService igniteCustomerService;
    private final IHazelcastService<Long, HazelcastCustomer> hazelcastService;

    public AuthController(IOracleCustomerService oracleCustomerService,
                          IIgniteCustomerService igniteCustomerService,
                          IHazelcastService<Long, HazelcastCustomer> hazelcastService) {
        this.oracleCustomerService = oracleCustomerService;
        this.igniteCustomerService = igniteCustomerService;
        this.hazelcastService = hazelcastService;

    }

    /**
     * Registers a new customer.
     * Önce Oracle DB’ye kayıt yapılır, ardından aynı müşteri Ignite DB’ye eklenir.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(@RequestBody RegisterRequest request) {
        logger.info("Received register request for email: {} in Oracle DB", request.getEmail());

        // Oracle DB'ye kayıt
        OracleCustomer customer = oracleCustomerService.register(request);
        logger.info("Customer registered in Oracle DB with email: {}", customer.getEmail());

        // Ignite DB'ye ekleme işlemi
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

        // 3️⃣ Hazelcast'e ekleme işlemi
        HazelcastCustomer hazelcastCustomer = new HazelcastCustomer(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getRole().toString(),
                customer.getStartDate(),
                customer.getAddress(),
                customer.getStatus()
        );
        hazelcastService.save(hazelcastCustomer.getId(), hazelcastCustomer);
        logger.info("Customer registered in Hazelcast cache: {}", customer.getEmail());

        return ResponseEntity.ok(new ApiResponse("Customer registered successfully!"));
    }

    /**
     * Authenticates a customer (login process).
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateCustomer(@RequestBody AuthRequest request) {
        logger.info("Received authentication request for email: {} in Oracle DB", request.getEmail());

        String token = oracleCustomerService.authenticateCustomer(request);
        logger.info("Authentication successful for email: {}. Token generated.", request.getEmail());

        return ResponseEntity.ok(new AuthResponse(token));
    }

    /**
     * Changes password for an existing customer.
     */
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        logger.info("Received change password request for email: {} in Oracle DB", request.getEmail());

        oracleCustomerService.changePassword(request);
        logger.info("Password changed successfully for email: {} in Oracle DB", request.getEmail());

        return ResponseEntity.ok(new ApiResponse("Password changed successfully!"));
    }
}
