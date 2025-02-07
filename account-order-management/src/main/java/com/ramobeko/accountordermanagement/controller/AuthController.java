package com.ramobeko.accountordermanagement.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ramobeko.accountordermanagement.model.dto.AuthRequest;
import com.ramobeko.accountordermanagement.model.dto.AuthResponse;
import com.ramobeko.accountordermanagement.model.dto.RegisterRequest;
import com.ramobeko.accountordermanagement.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    /**
     * 📌 Registers a new customer with encrypted password.
     * @param request Registration request data
     * @return ResponseEntity with success message
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerCustomer(@RequestBody RegisterRequest request) {
        authService.registerCustomer(request);
        return ResponseEntity.ok("Customer registered successfully!");
    }

    /**
     * 📌 Authenticates a customer (Login process)
     * @param request Authentication request with email & password
     * @return JWT Token if authentication is successful
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateCustomer(@RequestBody AuthRequest request) {
        String token = authService.authenticateCustomer(request);
        return ResponseEntity.ok(new AuthResponse());
    }

    /**
     * 📌 Changes password for an existing customer
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
        authService.changePassword(email, oldPassword, newPassword);
        return ResponseEntity.ok("Password changed successfully!");
    }

    /**
     * 📌 Retrieves customer details by email
     * @param email Customer email
     * @return ResponseEntity with customer details
     */
    @GetMapping("/customer-details")
    public ResponseEntity<?> getCustomerDetails(@RequestParam String email) {
        return ResponseEntity.ok(authService.getCustomerDetails(email));
    }
}