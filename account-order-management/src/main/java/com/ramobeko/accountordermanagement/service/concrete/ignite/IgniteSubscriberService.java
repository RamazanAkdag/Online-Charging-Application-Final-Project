package com.ramobeko.accountordermanagement.service.concrete.ignite;

import com.ramobeko.accountordermanagement.model.shared.OracleSubscriber;
import com.ramobeko.accountordermanagement.repository.ignite.IgniteSubscriberRepository;
import com.ramobeko.accountordermanagement.service.abstrct.ignite.IIgniteSubscriberService;
import com.ramobeko.accountordermanagement.util.mapper.ignite.IgniteSubscriberMapper;
import com.ramobeko.ignite.IgniteSubscriber;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IgniteSubscriberService implements IIgniteSubscriberService {

    private static final Logger logger = LogManager.getLogger(IgniteSubscriberService.class);
    private final IgniteSubscriberRepository repository;

    public IgniteSubscriberService(IgniteSubscriberRepository repository) {
        this.repository = repository;
    }

    @Override
    public void createFromOracle(OracleSubscriber oracleSubscriber) {
        if (oracleSubscriber == null || oracleSubscriber.getId() == null) {
            logger.error("Failed to create Ignite subscriber: Oracle subscriber is null or has no ID.");
            throw new IllegalArgumentException("Oracle subscriber and ID must not be null");
        }

        logger.info("Creating Ignite subscriber from Oracle subscriber with ID: {}", oracleSubscriber.getId());

        IgniteSubscriber igniteSubscriber = IgniteSubscriberMapper.fromOracle(oracleSubscriber);

        repository.save(Long.parseLong(igniteSubscriber.getPhoneNumber()), igniteSubscriber);
        logger.info("Ignite subscriber created successfully with ID: {}", igniteSubscriber.getId());
    }

    @Override
    public IgniteSubscriber getById(Long id) {
        logger.info("Fetching Ignite subscriber by ID: {}", id);
        return repository.findById(id).orElseThrow(() -> {
            logger.error("Subscriber not found in Ignite with ID: {}", id);
            return new IllegalArgumentException("Subscriber not found in Ignite with ID: " + id);
        });
    }

    @Override
    public IgniteSubscriber getByPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            logger.error("Failed to retrieve subscriber: Phone number is null or blank");
            return null;
        }

        IgniteSubscriber subscriber = repository.findByPhoneNumber(phoneNumber);
        if (subscriber != null) {
            logger.info("Subscriber retrieved successfully with phone number: {}", phoneNumber);
        } else {
            logger.warn("Subscriber with phone number {} not found", phoneNumber);
        }
        return subscriber;
    }

    @Override
    public void updateFromOracle(OracleSubscriber oracleSubscriber) {
        if (oracleSubscriber == null || oracleSubscriber.getId() == null) {
            logger.error("Failed to update Ignite subscriber: Oracle subscriber is null or has no ID.");
            throw new IllegalArgumentException("Oracle subscriber and ID must not be null");
        }

        logger.info("Updating Ignite subscriber with ID: {}", oracleSubscriber.getId());

        Optional<IgniteSubscriber> existingSubscriber = repository.findById(oracleSubscriber.getId());
        if (existingSubscriber.isPresent()) {
            IgniteSubscriber igniteSubscriber = IgniteSubscriberMapper.fromOracle(oracleSubscriber);
            repository.save(igniteSubscriber.getId(), igniteSubscriber);
            logger.info("Ignite subscriber updated successfully with ID: {}", igniteSubscriber.getId());
        } else {
            logger.warn("Subscriber with ID {} not found in Ignite, update skipped", oracleSubscriber.getId());
            throw new IllegalStateException("Subscriber with ID " + oracleSubscriber.getId() + " does not exist.");
        }
    }

    @Override
    public void delete(Long id) {
        logger.info("Deleting Ignite subscriber with ID: {}", id);
        Optional<IgniteSubscriber> existingSubscriber = repository.findById(id);

        if (existingSubscriber.isPresent()) {
            repository.deleteById(id);
            logger.info("Ignite subscriber deleted successfully with ID: {}", id);
        } else {
            logger.warn("Subscriber with ID {} not found in Ignite, delete skipped", id);
            throw new IllegalStateException("Subscriber with ID " + id + " does not exist.");
        }
    }
}
