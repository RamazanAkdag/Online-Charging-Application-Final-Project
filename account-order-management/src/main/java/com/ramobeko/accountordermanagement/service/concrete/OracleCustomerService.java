package com.ramobeko.accountordermanagement.service.concrete;

import com.ramobeko.accountordermanagement.model.dto.AuthRequest;
import com.ramobeko.accountordermanagement.model.dto.RegisterRequest;
import com.ramobeko.accountordermanagement.model.entity.oracle.OracleCustomer;
import com.ramobeko.accountordermanagement.repository.oracle.OracleCustomerRepository;
import com.ramobeko.accountordermanagement.security.JwtUtil;
import com.ramobeko.accountordermanagement.service.abstrct.oracle.IOracleCustomerService;
import com.ramobeko.accountordermanagement.util.model.Role;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OracleCustomerService implements IOracleCustomerService {

    private static final Logger logger = LogManager.getLogger(OracleCustomerService.class);

    private final OracleCustomerRepository oracleCustomerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public OracleCustomerService(OracleCustomerRepository oracleCustomerRepository,
                                 PasswordEncoder passwordEncoder,
                                 JwtUtil jwtUtil) {
        this.oracleCustomerRepository = oracleCustomerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public String authenticateCustomer(AuthRequest request) {
        logger.info("Authenticating customer with email: {} to Oracle DB", request.getEmail());

        OracleCustomer oracleCustomer = oracleCustomerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    logger.error("Authentication failed. Customer not found with email: {} in Oracle DB", request.getEmail());
                    return new UsernameNotFoundException("Invalid email or password");
                });

        if (!passwordEncoder.matches(request.getPassword(), oracleCustomer.getPassword())) {
            logger.error("Authentication failed for customer: {} when comparing credentials to Oracle DB", request.getEmail());
            throw new BadCredentialsException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(oracleCustomer.getEmail(), oracleCustomer.getRole().name());
        logger.info("Authentication successful for customer: {} from Oracle DB", request.getEmail());
        return token;
    }

    @Override
    public void create(OracleCustomer oracleCustomer) {
        logger.info("Creating new customer: {} in Oracle DB", oracleCustomer.getEmail());
        oracleCustomer.setPassword(passwordEncoder.encode(oracleCustomer.getPassword()));
        oracleCustomer.setStartDate(new Date());
        oracleCustomer.setStatus("ACTIVE");
        oracleCustomer.setRole(oracleCustomer.getRole() != null ? oracleCustomer.getRole() : Role.USER);
        oracleCustomerRepository.save(oracleCustomer);
        logger.info("Customer created successfully in Oracle DB: {}", oracleCustomer.getEmail());
    }

    @Override
    public OracleCustomer register(RegisterRequest request) {
        logger.info("Registering new customer: {} in Oracle DB", request.getEmail());

        OracleCustomer oracleCustomer = new OracleCustomer();
        oracleCustomer.setName(request.getName());
        oracleCustomer.setEmail(request.getEmail());
        oracleCustomer.setAddress(request.getAddress());
        oracleCustomer.setStatus("ACTIVE");
        oracleCustomer.setStartDate(new Date());
        oracleCustomer.setPassword(passwordEncoder.encode(request.getPassword()));
        oracleCustomer.setRole(request.getRole() != null ? request.getRole() : Role.USER);

        OracleCustomer savedOracleCustomer = oracleCustomerRepository.save(oracleCustomer);
        logger.info("Customer registered successfully in Oracle DB: {}", savedOracleCustomer.getEmail());
        return savedOracleCustomer;
    }

    @Override
    public OracleCustomer readById(Long id) {
        logger.info("Reading customer by ID: {} from Oracle DB", id);
        OracleCustomer oracleCustomer = oracleCustomerRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Customer not found with ID: {} in Oracle DB", id);
                    return new UsernameNotFoundException("Customer not found");
                });
        logger.info("Customer read successfully from Oracle DB: {}", oracleCustomer.getEmail());
        return oracleCustomer;
    }

    @Override
    public List<OracleCustomer> readAll() {
        logger.info("Reading all customers from Oracle DB");
        List<OracleCustomer> oracleCustomers = oracleCustomerRepository.findAll();
        logger.info("Total customers found in Oracle DB: {}", oracleCustomers.size());
        return oracleCustomers;
    }

    @Override
    public void update(OracleCustomer oracleCustomer) {
        logger.info("Updating customer with ID: {} in Oracle DB", oracleCustomer.getId());
        if (!oracleCustomerRepository.existsById(oracleCustomer.getId())) {
            logger.error("Customer not found with ID: {} in Oracle DB", oracleCustomer.getId());
            throw new UsernameNotFoundException("Customer not found");
        }
        oracleCustomer.setPassword(passwordEncoder.encode(oracleCustomer.getPassword()));
        oracleCustomerRepository.save(oracleCustomer);
        logger.info("Customer updated successfully in Oracle DB with ID: {}", oracleCustomer.getId());
    }

    @Override
    public void delete(Long id) {
        logger.info("Deleting customer with ID: {} from Oracle DB", id);
        oracleCustomerRepository.deleteById(id);
        logger.info("Customer deleted successfully from Oracle DB with ID: {}", id);
    }

    @Override
    public void changePassword(String email, String oldPassword, String newPassword) {
        logger.info("Changing password for customer: {} in Oracle DB", email);
        OracleCustomer oracleCustomer = oracleCustomerRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("Customer not found with email: {} in Oracle DB", email);
                    return new UsernameNotFoundException("Customer not found");
                });

        if (!passwordEncoder.matches(oldPassword, oracleCustomer.getPassword())) {
            logger.error("Incorrect old password for customer: {} in Oracle DB", email);
            throw new IllegalArgumentException("Invalid old password");
        }

        oracleCustomer.setPassword(passwordEncoder.encode(newPassword));
        oracleCustomerRepository.save(oracleCustomer);
        logger.info("Password changed successfully for customer: {} in Oracle DB", email);
    }

    @Override
    public OracleCustomer getCustomerDetails(String email) {
        logger.info("Retrieving customer details for email: {} from Oracle DB", email);
        OracleCustomer oracleCustomer = oracleCustomerRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("Customer not found with email: {} in Oracle DB", email);
                    return new UsernameNotFoundException("Customer not found");
                });
        logger.info("Customer details retrieved successfully from Oracle DB for email: {}", email);
        return oracleCustomer;
    }
}