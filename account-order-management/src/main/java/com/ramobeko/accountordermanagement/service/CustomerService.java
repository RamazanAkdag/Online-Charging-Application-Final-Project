package com.ramobeko.accountordermanagement.service;

import com.ramobeko.accountordermanagement.model.entity.Customer;
import com.ramobeko.accountordermanagement.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService implements ICustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * 📌 Adds a new customer (Calls the Oracle stored procedure)
     *
     * @param name Customer name
     * @param email Customer email address (must be unique)
     * @param address Customer address
     * @param status Customer status (ACTIVE or INACTIVE)
     */
    @Transactional
    @Override
    public void addCustomer(String name, String email, String address, String status) {
        customerRepository.addCustomer(name, email, address, status);
    }

    /**
     * 📌 Retrieves all customers (Calls the Oracle stored procedure)
     *
     * @return List of all customers.
     */
    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.getAllCustomers();
    }
}