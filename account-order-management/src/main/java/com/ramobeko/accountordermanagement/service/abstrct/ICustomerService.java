package com.ramobeko.accountordermanagement.service.abstrct;

import com.ramobeko.accountordermanagement.model.entity.oracle.Customer;

import java.util.List;

public interface ICustomerService {

    /**
     * 📌 Adds a new customer to the system.
     *
     * @param name Customer's full name
     * @param email Customer's email (must be unique)
     * @param address Customer's physical address
     * @param status Customer's status (ACTIVE or INACTIVE)
     */
    void addCustomer(String name, String email, String address, String status);

    /**
     * 📌 Retrieves all customers from the system.
     *
     * @return List of all registered customers.
     */
    List<Customer> getAllCustomers();
}