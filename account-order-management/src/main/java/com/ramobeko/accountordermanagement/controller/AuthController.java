package com.ramobeko.accountordermanagement.controller;

import com.ramobeko.accountordermanagement.service.abstrct.ignite.IIgniteCustomerService;
import com.ramobeko.accountordermanagement.service.abstrct.oracle.IOracleCustomerService;
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
    private final IOracleCustomerService oracleCustomerService;
    private final IIgniteCustomerService igniteCustomerService;

    public AuthController(IOracleCustomerService oracleCustomerService, IIgniteCustomerService igniteCustomerService) {

        this.oracleCustomerService = oracleCustomerService;
        this.igniteCustomerService = igniteCustomerService;
    }


    /**
     *  Registers a new customer with encrypted password.
     * @param request Registration request data
     * @return ResponseEntity with success message
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerCustomer(@RequestBody RegisterRequest request) {

        oracleCustomerService.register(request);
        igniteCustomerService.register(request);

        //hazelcastCustomerService.register(request);

        return ResponseEntity.ok("Customer registered successfully!");
    }

    /**
     *  Authenticates a customer (Login process)
     * @param request Authentication request with email & password
     * @return JWT Token if authentication is successful
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateCustomer(@RequestBody AuthRequest request) {
        String token = oracleCustomerService.authenticateCustomer(request);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    /**
     *  Changes password for an existing customer
     * @param email Customer email
     * @param oldPassword Old password for verification
     * @param newPassword New password to update
     * @return ResponseEntity with success message
     */
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestParam String email,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        oracleCustomerService.changePassword(email, oldPassword, newPassword);

        return ResponseEntity.ok("Password changed successfully!");
    }
}