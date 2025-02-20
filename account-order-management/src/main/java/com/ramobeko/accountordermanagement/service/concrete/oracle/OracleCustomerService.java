package com.ramobeko.accountordermanagement.service.concrete.oracle;

import com.ramobeko.accountordermanagement.model.dto.OracleCustomerDTO;
import com.ramobeko.accountordermanagement.model.dto.RegisterRequest;
import com.ramobeko.accountordermanagement.model.dto.request.AuthRequest;
import com.ramobeko.accountordermanagement.model.dto.request.ChangePasswordRequest;
import com.ramobeko.accountordermanagement.model.entity.oracle.OracleCustomer;
import com.ramobeko.accountordermanagement.repository.oracle.OracleCustomerRepository;
import com.ramobeko.accountordermanagement.security.JwtUtil;
import com.ramobeko.accountordermanagement.service.abstrct.oracle.IOracleCustomerService;
import com.ramobeko.accountordermanagement.util.model.Role;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.BadCredentialsException;
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
        logger.info("Authenticating customer with email: {}", request.getEmail());

        OracleCustomer customer = findCustomerByEmail(request.getEmail());

        if (!passwordEncoder.matches(request.getPassword(), customer.getPassword())) {
            logger.error("Authentication failed for customer: {}", request.getEmail());
            throw new BadCredentialsException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(customer.getId(), customer.getEmail(), customer.getRole().name());
        logger.info("Authentication successful for customer: {}", request.getEmail());
        return token;
    }


    @Override
    public void create(Long id, OracleCustomerDTO oracleCustomerDTO) {
        /**
         * ⚠️ This method is a wrapper for `register()`
         *
         * - Directly redirects to `register()`, ensuring password encryption and validation.
         * - If additional logic is required, modify this method accordingly.
         */
        logger.info("Redirecting create() to register() for customer: {}", oracleCustomerDTO.getEmail());
        //register(request);
    }


    @Override
    public OracleCustomer register(RegisterRequest request) {
        logger.info("Registering new customer: {} in Oracle DB", request.getEmail());

        if (oracleCustomerRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        OracleCustomer customer = new OracleCustomer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setAddress(request.getAddress());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setStartDate(new Date());
        customer.setStatus("ACTIVE");
        customer.setRole(request.getRole() != null ? request.getRole() : Role.USER);

        OracleCustomer savedCustomer = oracleCustomerRepository.save(customer);
        logger.info("Customer registered successfully in Oracle DB: {}", savedCustomer.getEmail());

        return savedCustomer;
    }

    @Override
    public OracleCustomer readById(Long id) {
        logger.info("Reading customer by ID: {}", id);
        return findCustomerById(id);
    }



    @Override
    public List<OracleCustomer> readAll() {
        logger.info("Reading all customers from Oracle DB");
        return oracleCustomerRepository.findAll();
    }



    @Override
    public void update(OracleCustomerDTO oracleCustomerDTO) {
        logger.info("Updating customer with ID: {}", oracleCustomerDTO.getId());

        OracleCustomer existingCustomer = findCustomerById(oracleCustomerDTO.getId());

        existingCustomer.setName(oracleCustomerDTO.getName());
        existingCustomer.setAddress(oracleCustomerDTO.getAddress());
        existingCustomer.setStatus(oracleCustomerDTO.getStatus());

        OracleCustomer updatedCustomer = oracleCustomerRepository.save(existingCustomer);
        logger.info("Customer updated successfully in Oracle DB with ID: {}", updatedCustomer.getId());

    }


    @Override
    public void delete(Long id) {
        logger.info("Deleting customer with ID: {}", id);

        if (!oracleCustomerRepository.existsById(id)) {
            logger.error("Customer not found with ID: {}", id);
            throw new IllegalArgumentException("Customer not found");
        }

        oracleCustomerRepository.deleteById(id);
        logger.info("Customer deleted successfully from Oracle DB with ID: {}", id);
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        logger.info("Changing password for customer: {}", request.getEmail());

        OracleCustomer customer = findCustomerByEmail(request.getEmail());

        if (!passwordEncoder.matches(request.getOldPassword(), customer.getPassword())) {
            logger.error("Incorrect old password for customer: {}", request.getEmail());
            throw new IllegalArgumentException("Invalid old password");
        }

        customer.setPassword(passwordEncoder.encode(request.getNewPassword()));
        oracleCustomerRepository.save(customer);
        logger.info("Password changed successfully for customer: {}", request.getEmail());
    }

    @Override
    public OracleCustomer getCustomerDetails(String email) {
        logger.info("Retrieving customer details for email: {}", email);
        return findCustomerByEmail(email);
    }

    public OracleCustomer findCustomerByEmail(String email) {
        return oracleCustomerRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("Customer not found with email: {}", email);
                    return new IllegalArgumentException("Customer not found with email: " + email);
                });
    }

    private OracleCustomer findCustomerById(Long id) {
        return oracleCustomerRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Customer not found with ID: {}", id);
                    return new IllegalArgumentException("Customer not found with ID: " + id);
                });
    }



}
