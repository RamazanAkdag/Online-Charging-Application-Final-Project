package com.ramobeko.accountordermanagement.service.concrete.ignite;

import com.ramobeko.accountordermanagement.model.entity.ignite.IgniteCustomer;
import com.ramobeko.accountordermanagement.repository.ignite.IgniteCustomerRepository;
import com.ramobeko.accountordermanagement.service.abstrct.ignite.IIgniteCustomerService;
import com.ramobeko.accountordermanagement.util.helper.IgniteCustomerInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class IgniteCustomerService implements IIgniteCustomerService {

    private static final Logger logger = LogManager.getLogger(IgniteCustomerService.class);

    private final IgniteCustomerRepository igniteCustomerRepository;
    private final IgniteCustomerInitializer igniteCustomerInitializer;

    public IgniteCustomerService(IgniteCustomerRepository igniteCustomerRepository,
                                 PasswordEncoder passwordEncoder,
                                 IgniteCustomerInitializer igniteCustomerInitializer) {
        this.igniteCustomerRepository = igniteCustomerRepository;
        this.igniteCustomerInitializer = igniteCustomerInitializer;
    }

    @Override
    public IgniteCustomer register(IgniteCustomer igniteCustomer) {
        logger.info("Registering new customer to ignite: {}", igniteCustomer.getEmail());
        igniteCustomerInitializer.initializeCustomer(igniteCustomer);
        IgniteCustomer savedCustomer = igniteCustomerRepository.save(igniteCustomer.getId(), igniteCustomer);
        logger.info("✅ Customer registered successfully to ignite: {}", savedCustomer.getEmail());
        return savedCustomer;
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
