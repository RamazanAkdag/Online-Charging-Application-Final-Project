package com.ramobeko.accountordermanagement.service.abstrct;

import com.ramobeko.accountordermanagement.model.dto.AuthRequest;
import com.ramobeko.accountordermanagement.model.dto.RegisterRequest;
import com.ramobeko.accountordermanagement.model.entity.oracle.Customer;

public interface IAuthService {

    /**
     * Registers a new customer with an encrypted password.
     * @param request Registration request data
     */
    void registerCustomer(RegisterRequest request);

    /**
     * Authenticates a customer and returns a JWT token if successful.
     * @param request Authentication request (email & password)
     * @return JWT Token if authentication is successful
     */
    String authenticateCustomer(AuthRequest request);

    /**
     * Changes the password for an existing customer.
     * @param email Customer email
     * @param oldPassword Old password for verification
     * @param newPassword New password to update
     */
    void changePassword(String email, String oldPassword, String newPassword);

    /**
     * Retrieves customer details based on email.
     * @param email Customer email
     * @return Customer entity
     */
    Customer getCustomerDetails(String email);
}