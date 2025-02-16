package com.ramobeko.accountordermanagement.service.concrete.ignite;


import com.ramobeko.accountordermanagement.model.entity.ignite.IgniteCustomer;
import com.ramobeko.accountordermanagement.repository.ignite.IgniteCustomerRepository;
import com.ramobeko.accountordermanagement.service.abstrct.ignite.IIgniteCustomerService;
import com.ramobeko.accountordermanagement.util.model.Role;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class IgniteCustomerService implements IIgniteCustomerService {

    private static final Logger logger = LogManager.getLogger(IgniteCustomerService.class);

    private final IgniteCustomerRepository igniteCustomerRepository;
    private final PasswordEncoder passwordEncoder;

    public IgniteCustomerService(IgniteCustomerRepository igniteCustomerRepository, PasswordEncoder passwordEncoder) {
        this.igniteCustomerRepository = igniteCustomerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public IgniteCustomer register(IgniteCustomer igniteCustomer) {
        logger.info(" Registering new customer to ignite: {}", igniteCustomer.getEmail());

        if (igniteCustomer.getId() == null) {
            igniteCustomer.setId(System.currentTimeMillis());
        }
        if (igniteCustomer.getStatus() == null) {
            igniteCustomer.setStatus("ACTIVE");
        }
        if (igniteCustomer.getStartDate() == null) {
            igniteCustomer.setStartDate(new Date());
        }
        if (igniteCustomer.getRole() == null) {
            igniteCustomer.setRole(Role.USER);
        }
        if (igniteCustomer.getPassword() != null) {
            igniteCustomer.setPassword(passwordEncoder.encode(igniteCustomer.getPassword()));
        }

        IgniteCustomer savedigniteCustomer = igniteCustomerRepository.save(igniteCustomer.getId(),igniteCustomer);
        logger.info("✅ Customer registered successfully to ignite: {}", savedigniteCustomer.getEmail());
        return savedigniteCustomer;
    }

    @Override
    public void create(IgniteCustomer entity) {
        logger.info("📝 Creating new customer: {}", entity.getEmail());
        igniteCustomerRepository.save(entity);
        logger.info("✅ Customer created: {}", entity.getEmail());
    }

    @Override
    public void update(IgniteCustomer entity) {
        logger.info("🔄 Updating customer: {}", entity.getId());

        if (igniteCustomerRepository.existsById(entity.getId())) {
            igniteCustomerRepository.save(entity);
            logger.info("✅ Customer updated: {}", entity.getId());
        } else {
            logger.error("❌ Customer not found with ID: {}", entity.getId());
            throw new RuntimeException("Customer not found with ID: " + entity.getId());
        }
    }

    @Override
    public void delete(Long id) {
        logger.info("🗑 Deleting customer with ID: {}", id);
        igniteCustomerRepository.deleteById(id);
        logger.info("✅ Customer deleted: {}", id);
    }
}