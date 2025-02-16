package com.ramobeko.accountordermanagement.service.concrete.oracle;

import com.ramobeko.accountordermanagement.model.dto.request.SubscriberRequest;
import com.ramobeko.accountordermanagement.model.entity.oracle.OracleCustomer;
import com.ramobeko.accountordermanagement.model.shared.OracleSubscriber;
import com.ramobeko.accountordermanagement.repository.oracle.OracleCustomerRepository;
import com.ramobeko.accountordermanagement.repository.oracle.OracleSubscriberRepository;
import com.ramobeko.accountordermanagement.service.abstrct.oracle.IOracleSubscriberService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OracleSubscriberService implements IOracleSubscriberService {

    private static final Logger logger = LogManager.getLogger(OracleSubscriberService.class);
    private final OracleSubscriberRepository subscriberRepository;
    private final OracleCustomerRepository customerRepository;

    public OracleSubscriberService(OracleSubscriberRepository subscriberRepository, OracleCustomerRepository customerRepository) {
        this.subscriberRepository = subscriberRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public List<OracleSubscriber> readAll() {
        logger.info("Fetching all subscribers from database");
        return subscriberRepository.findAll();
    }

    @Override
    public void create(SubscriberRequest request) {
        logger.info("Creating new subscriber with phone number: {}", request.getPhoneNumber());

        OracleCustomer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + request.getCustomerId()));

        OracleSubscriber subscriber = new OracleSubscriber();
        subscriber.setCustomer(customer);
        subscriber.setPhoneNumber(request.getPhoneNumber());
        subscriber.setStartDate(request.getStartDate() != null ? request.getStartDate() : new java.util.Date());
        subscriber.setStatus(request.getStatus());

        subscriberRepository.save(subscriber);
        logger.info("Subscriber created successfully with phone number: {}", request.getPhoneNumber());
    }


    @Override
    public OracleSubscriber readById(Long id) {
        logger.info("Fetching subscriber by ID: {}", id);
        return findSubscriberById(id);
    }

    @Override
    public void update(SubscriberRequest request) {
        logger.info("Updating subscriber with ID: {}", request.getCustomerId());

        OracleSubscriber subscriber = findSubscriberById(request.getCustomerId());

        subscriber.setPhoneNumber(request.getPhoneNumber());
        subscriber.setStartDate(request.getStartDate());
        subscriber.setEndDate(request.getEndDate());
        subscriber.setStatus(request.getStatus());

        subscriberRepository.save(subscriber);
        logger.info("Subscriber updated successfully with ID: {}", subscriber.getId());
    }

    @Override
    public void delete(Long id) {
        logger.info("Deleting subscriber with ID: {}", id);

        if (!subscriberRepository.existsById(id)) {
            logger.error("Subscriber not found with ID: {}", id);
            throw new IllegalArgumentException("Subscriber not found");
        }

        subscriberRepository.deleteById(id);
        logger.info("Subscriber deleted successfully with ID: {}", id);
    }

    private OracleSubscriber findSubscriberById(Long id) {
        return subscriberRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Subscriber not found with ID: {}", id);
                    return new IllegalArgumentException("Subscriber not found with ID: " + id);
                });
    }
}
