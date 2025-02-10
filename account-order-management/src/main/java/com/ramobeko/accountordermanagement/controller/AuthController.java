package com.ramobeko.accountordermanagement.controller;

import com.ramobeko.accountordermanagement.model.entity.ignite.IgniteCustomer;
import com.ramobeko.accountordermanagement.service.abstrct.ignite.IIgniteCustomerService;
import com.ramobeko.accountordermanagement.service.abstrct.oracle.IOracleCustomerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ramobeko.accountordermanagement.model.dto.AuthRequest;
import com.ramobeko.accountordermanagement.model.dto.AuthResponse;
import com.ramobeko.accountordermanagement.model.dto.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LogManager.getLogger(AuthController.class);

    private final IOracleCustomerService oracleCustomerService;
    private final IIgniteCustomerService igniteCustomerService;

    public AuthController(IOracleCustomerService oracleCustomerService,
                          IIgniteCustomerService igniteCustomerService) {
        this.oracleCustomerService = oracleCustomerService;
        this.igniteCustomerService = igniteCustomerService;
    }

    /**
     * Registers a new customer.
     * Önce Oracle DB’ye kayıt yapılır, ardından aynı müşteri Ignite DB’ye eklenir.
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerCustomer(@RequestBody RegisterRequest request) {
        logger.info("Received register request for email: {} to Oracle DB", request.getEmail());


        var customer = oracleCustomerService.register(request);
        logger.info("Customer registered in Oracle DB with email: {}", customer.getEmail());
        /*logger.info("Registering same customer in Ignite DB for email: {}", customer.getEmail());


        IgniteCustomer igniteCustomer = new IgniteCustomer();
        igniteCustomer.setId(customer.getId());
        igniteCustomer.setName(customer.getName());
        igniteCustomer.setRole(customer.getRole());
        igniteCustomer.setPassword(customer.getPassword());
        igniteCustomer.setEmail(customer.getEmail());
        igniteCustomer.setStartDate(customer.getStartDate());
        igniteCustomer.setStatus(customer.getStatus());
        igniteCustomer.setAddress(customer.getAddress());

        igniteCustomerService.register(igniteCustomer);
        logger.info("Customer registered successfully in both Oracle and Ignite DB: {}", customer.getEmail());*/

        return ResponseEntity.ok("Customer registered successfully!");
    }

    /**
     * Authenticates a customer (login process).
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateCustomer(@RequestBody AuthRequest request) {
        logger.info("Received authentication request for email: {} to Oracle DB", request.getEmail());

        String token = oracleCustomerService.authenticateCustomer(request);
        logger.info("Authentication successful for email: {}. Token generated.", request.getEmail());

        return ResponseEntity.ok(new AuthResponse(token));
    }

    /**
     * Changes password for an existing customer.
     */
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestParam String email,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        logger.info("Received change password request for email: {} to Oracle DB", email);

        oracleCustomerService.changePassword(email, oldPassword, newPassword);
        logger.info("Password changed successfully for email: {} in Oracle DB", email);

        return ResponseEntity.ok("Password changed successfully!");
    }
}