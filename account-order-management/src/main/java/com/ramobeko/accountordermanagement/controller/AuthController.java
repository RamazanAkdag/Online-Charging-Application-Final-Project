package com.ramobeko.accountordermanagement.controller;

import com.ramobeko.accountordermanagement.model.dto.*;
import com.ramobeko.accountordermanagement.model.dto.request.AuthRequest;
import com.ramobeko.accountordermanagement.model.dto.request.ChangePasswordRequest;
import com.ramobeko.accountordermanagement.model.dto.request.ForgotPasswordRequest;
import com.ramobeko.accountordermanagement.model.dto.request.ResetPasswordRequest;
import com.ramobeko.accountordermanagement.model.dto.response.ApiResponse;
import com.ramobeko.accountordermanagement.model.dto.response.AuthResponse;
import com.ramobeko.accountordermanagement.model.entity.ignite.IgniteCustomer;
import com.ramobeko.accountordermanagement.model.entity.oracle.OracleCustomer;
import com.ramobeko.accountordermanagement.service.ForgotPasswordService;
import com.ramobeko.accountordermanagement.service.abstrct.ignite.IIgniteCustomerService;
import com.ramobeko.accountordermanagement.service.abstrct.oracle.IOracleCustomerService;
import com.ramobeko.accountordermanagement.util.mapper.ignite.OracleToIgniteCustomerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final IOracleCustomerService oracleCustomerService;
    private final IIgniteCustomerService igniteCustomerService;

    private final ForgotPasswordService forgotPasswordService;

    public AuthController(IOracleCustomerService oracleCustomerService,
                          IIgniteCustomerService igniteCustomerService, ForgotPasswordService forgotPasswordService) {
        this.oracleCustomerService = oracleCustomerService;
        this.igniteCustomerService = igniteCustomerService;

        this.forgotPasswordService = forgotPasswordService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(@RequestBody RegisterRequest request) {
        logger.info("Received register request for email: {} in Oracle DB", request.getEmail());

        OracleCustomer customer = oracleCustomerService.register(request);
        logger.info("Customer registered in Oracle DB with email: {}", customer.getEmail());

        // Mapper kullanılarak IgniteCustomer oluşturuluyor.
        IgniteCustomer igniteCustomer = OracleToIgniteCustomerMapper.map(customer);
        igniteCustomerService.register(igniteCustomer);

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

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        logger.info("Şifre sıfırlama talebi alındı: {}", request.getEmail());

        boolean mailSent = forgotPasswordService.processForgotPassword(request.getEmail());

        if (mailSent) {
            return ResponseEntity.ok(new ApiResponse("Şifre sıfırlama bağlantısı e-posta adresinize gönderildi."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("E-posta adresi sistemde bulunamadı."));
        }
    }



}
