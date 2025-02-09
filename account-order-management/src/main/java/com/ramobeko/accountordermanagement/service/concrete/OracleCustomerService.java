package com.ramobeko.accountordermanagement.service.concrete;

import com.ramobeko.accountordermanagement.model.dto.AuthRequest;
import com.ramobeko.accountordermanagement.model.dto.RegisterRequest;
import com.ramobeko.accountordermanagement.model.entity.Customer;
import com.ramobeko.accountordermanagement.repository.oracle.OracleCustomerRepository;
import com.ramobeko.accountordermanagement.security.JwtUtil;
import com.ramobeko.accountordermanagement.service.abstrct.oracle.IOracleCustomerService;
import com.ramobeko.accountordermanagement.util.model.Role;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OracleCustomerService implements IOracleCustomerService {
    private final OracleCustomerRepository oracleCustomerRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    public OracleCustomerService(OracleCustomerRepository oracleCustomerRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.oracleCustomerRepository = oracleCustomerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public String authenticateCustomer(AuthRequest request) {
        Customer customer = oracleCustomerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid email or password"));


        if (!passwordEncoder.matches(request.getPassword(), customer.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        return jwtUtil.generateToken(customer.getEmail(), customer.getRole().name());
    }

    @Override
    public void create(Customer customer) {
        customer.setPassword(passwordEncoder.encode(customer.getPassword())); // Encrypt password
        customer.setStartDate(new Date());
        customer.setStatus("ACTIVE");
        customer.setRole(customer.getRole() != null ? customer.getRole() : Role.USER); // Default role
        oracleCustomerRepository.save(customer);
    }

    @Override
    public void register(RegisterRequest request) {
        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setAddress(request.getAddress());
        customer.setStatus("ACTIVE");
        customer.setStartDate(new Date());
        customer.setPassword(passwordEncoder.encode(request.getPassword())); // Encrypt password
        customer.setRole(request.getRole() != null ? request.getRole() : Role.USER); // Default role

        oracleCustomerRepository.save(customer);
    }

    @Override
    public Customer readById(Long id) {
        return oracleCustomerRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Customer not found"));
    }

    @Override
    public List<Customer> readAll() {
        return oracleCustomerRepository.findAll();
    }

    @Override
    public void update(Customer customer) {
        if (!oracleCustomerRepository.existsById(customer.getId())) {
            throw new UsernameNotFoundException("Customer not found");
        }
        customer.setPassword(passwordEncoder.encode(customer.getPassword())); // Encrypt new password
        oracleCustomerRepository.save(customer);
    }

    @Override
    public void delete(Long id) {
        oracleCustomerRepository.deleteById(id);
    }

    @Override
    public void changePassword(String email, String oldPassword, String newPassword) {
        Customer customer = oracleCustomerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Customer not found"));

        if (!passwordEncoder.matches(oldPassword, customer.getPassword())) {
            throw new IllegalArgumentException("Invalid old password");
        }

        customer.setPassword(passwordEncoder.encode(newPassword));
        oracleCustomerRepository.save(customer);
    }

    @Override
    public Customer getCustomerDetails(String email) {
        return oracleCustomerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Customer not found"));
    }


}
