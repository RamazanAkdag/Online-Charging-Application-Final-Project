package com.ramobeko.accountordermanagement.service.concrete;

import com.ramobeko.accountordermanagement.model.dto.AuthRequest;
import com.ramobeko.accountordermanagement.model.dto.RegisterRequest;
import com.ramobeko.accountordermanagement.model.entity.oracle.Customer;
import com.ramobeko.accountordermanagement.repository.CustomerRepository;
import com.ramobeko.accountordermanagement.security.JwtUtil;
import com.ramobeko.accountordermanagement.service.abstrct.IAuthService;
import com.ramobeko.accountordermanagement.util.model.Role;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class AuthService implements IAuthService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }


    @Override
    public void registerCustomer(RegisterRequest request) {
        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setAddress(request.getAddress());
        customer.setStatus("ACTIVE");
        customer.setStartDate(new Date());
        customer.setPassword(passwordEncoder.encode(request.getPassword())); // Encrypt password
        customer.setRole(request.getRole() != null ? request.getRole() : Role.USER); // Default role

        customerRepository.save(customer);
    }

    @Override
    public String authenticateCustomer(AuthRequest request) {
        Customer customer = customerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid email or password"));


        if (!passwordEncoder.matches(request.getPassword(), customer.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        return jwtUtil.generateToken(customer.getEmail(), customer.getRole().name());
    }

    @Override
    public void changePassword(String email, String oldPassword, String newPassword) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Customer not found"));

        if (!passwordEncoder.matches(oldPassword, customer.getPassword())) {
            throw new BadCredentialsException("Invalid old password");
        }

        customer.setPassword(passwordEncoder.encode(newPassword)); // Encrypt new password
        customerRepository.save(customer);
    }

    @Override
    public Customer getCustomerDetails(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Customer not found"));
    }

}