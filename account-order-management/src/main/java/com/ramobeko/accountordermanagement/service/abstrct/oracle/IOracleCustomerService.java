package com.ramobeko.accountordermanagement.service.abstrct.oracle;

import com.ramobeko.accountordermanagement.model.dto.AuthRequest;
import com.ramobeko.accountordermanagement.model.dto.RegisterRequest;
import com.ramobeko.accountordermanagement.model.entity.Customer;

public interface IOracleCustomerService extends IOracleService<Customer> {

    /**
     * Authenticates a customer and returns a JWT token if successful.
     * @param request Authentication request (email & password)
     * @return JWT Token if authentication is successful
     */
    String authenticateCustomer(AuthRequest request);
    void register(RegisterRequest request);
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
