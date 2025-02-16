package com.ramobeko.accountordermanagement.service.abstrct.oracle;

import com.ramobeko.accountordermanagement.model.dto.request.AuthRequest;
import com.ramobeko.accountordermanagement.model.dto.request.ChangePasswordRequest;
import com.ramobeko.accountordermanagement.model.dto.OracleCustomerDTO;
import com.ramobeko.accountordermanagement.model.dto.RegisterRequest;
import com.ramobeko.accountordermanagement.model.entity.oracle.OracleCustomer;

public interface IOracleCustomerService extends IOracleService<OracleCustomer,OracleCustomerDTO> {

    /**
     * Authenticates a customer and returns a JWT token if successful.
     * @param request Authentication request (email & password)
     * @return JWT Token if authentication is successful
     */
    String authenticateCustomer(AuthRequest request);

    /**
     * Registers a customer, saves the same details to Ignite, and returns the registered customer.
     * @param request Registration request containing customer details.
     * @return Registered Customer DTO.
     */
    OracleCustomer register(RegisterRequest request);

    /**
     * Changes the password for an existing customer.
     * @param changePasswordRequest New password info to update
     */
    void changePassword(ChangePasswordRequest changePasswordRequest);

    /**
     * Retrieves customer details based on email.
     * @param email Customer email
     * @return Customer DTO
     */
    OracleCustomer getCustomerDetails(String email);
}
