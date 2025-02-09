package com.ramobeko.accountordermanagement.controller;

import com.ramobeko.accountordermanagement.service.abstrct.oracle.IOracleCustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final IOracleCustomerService oracleCustomerService;

    public CustomerController(IOracleCustomerService oracleCustomerService) {
        this.oracleCustomerService = oracleCustomerService;
    }

    /**
     * 📌 Retrieves customer details by email
     * @param email Customer email
     * @return ResponseEntity with customer details
     */
    @GetMapping("/details")
    public ResponseEntity<?> getCustomerDetails(@RequestParam String email) {
        return ResponseEntity.ok(oracleCustomerService.getCustomerDetails(email));
    }
}
