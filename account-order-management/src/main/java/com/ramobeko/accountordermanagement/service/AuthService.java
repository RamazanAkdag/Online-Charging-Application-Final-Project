package com.ramobeko.accountordermanagement.service;

import com.ramobeko.accountordermanagement.model.dto.AuthRequest;
import com.ramobeko.accountordermanagement.model.dto.RegisterRequest;
import com.ramobeko.accountordermanagement.model.entity.Customer;
import com.ramobeko.accountordermanagement.repository.CustomerRepository;
import com.ramobeko.accountordermanagement.security.JwtUtil;
import com.ramobeko.accountordermanagement.util.model.Role;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class AuthService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 📌 Registers a new customer with an encrypted password.
     * @param request Registration request data
     */
    public void registerCustomer(RegisterRequest request) {
        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setAddress(request.getAddress());
        customer.setPassword(passwordEncoder.encode(request.getPassword())); // Encrypt password
        customer.setRole(request.getRole() != null ? request.getRole() : Role.USER); // Default role

        customerRepository.save(customer);
    }

    /**
     * 📌 Authenticates a customer and returns a JWT token if successful.
     * @param request Authentication request (email & password)
     * @return JWT Token if authentication is successful
     */
    public String authenticateCustomer(AuthRequest request) {
        Customer customer = customerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), customer.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        return jwtUtil.generateToken(customer.getEmail(), customer.getRole().name());
    }

    /**
     * 📌 Changes the password for an existing customer.
     * @param email Customer email
     * @param oldPassword Old password for verification
     * @param newPassword New password to update
     */
    public void changePassword(String email, String oldPassword, String newPassword) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Customer not found"));

        if (!passwordEncoder.matches(oldPassword, customer.getPassword())) {
            throw new BadCredentialsException("Invalid old password");
        }

        customer.setPassword(passwordEncoder.encode(newPassword)); // Encrypt new password
        customerRepository.save(customer);
    }

    /**
     * 📌 Retrieves customer details based on email.
     * @param email Customer email
     * @return Customer entity
     */
    public Customer getCustomerDetails(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Customer not found"));
    }

}