package com.ramobeko.accountordermanagement.service.concrete.oracle;

import com.ramobeko.accountordermanagement.model.dto.request.SubscriberRequest;
import com.ramobeko.accountordermanagement.model.dto.request.SubscriberUpdateRequest;
import com.ramobeko.accountordermanagement.model.entity.oracle.OracleBalance;
import com.ramobeko.accountordermanagement.model.entity.oracle.OracleCustomer;
import com.ramobeko.accountordermanagement.model.entity.oracle.OraclePackage;
import com.ramobeko.accountordermanagement.model.shared.OracleSubscriber;
import com.ramobeko.accountordermanagement.repository.oracle.OracleBalanceRepository;
import com.ramobeko.accountordermanagement.repository.oracle.OracleCustomerRepository;
import com.ramobeko.accountordermanagement.repository.oracle.OraclePackageRepository;
import com.ramobeko.accountordermanagement.repository.oracle.OracleSubscriberRepository;
import com.ramobeko.accountordermanagement.service.abstrct.oracle.IOracleSubscriberService;
import com.ramobeko.accountordermanagement.util.generator.ITelephoneNumberGenerator;
import com.ramobeko.accountordermanagement.util.mapper.oracle.OracleSubscriberMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class OracleSubscriberService implements IOracleSubscriberService {

    private static final Logger logger = LogManager.getLogger(OracleSubscriberService.class);

    private final OracleSubscriberRepository subscriberRepository;
    private final OracleCustomerRepository customerRepository;
    private final OracleBalanceRepository balanceRepository;
    private final OraclePackageRepository packageRepository;
    private final ITelephoneNumberGenerator telephoneNumberGenerator;

    private final OracleSubscriberMapper oracleSubscriberMapper;

    public OracleSubscriberService(OracleSubscriberRepository subscriberRepository,
                                   OracleCustomerRepository customerRepository,
                                   OracleBalanceRepository balanceRepository,
                                   OraclePackageRepository packageRepository,
                                   ITelephoneNumberGenerator telephoneNumberGenerator,
                                   OracleSubscriberMapper oracleSubscriberMapper) {
        this.subscriberRepository = subscriberRepository;
        this.customerRepository = customerRepository;
        this.balanceRepository = balanceRepository;
        this.packageRepository = packageRepository;
        this.telephoneNumberGenerator = telephoneNumberGenerator;
        this.oracleSubscriberMapper = oracleSubscriberMapper;
    }

    @Override
    public List<OracleSubscriber> readAll() {
        logger.info("Fetching all subscribers from database");
        return subscriberRepository.findAll();
    }

    @Override
    public void create(Long id, SubscriberRequest request) {
        logger.info("Creating new subscriber for userId: {}", id);

        OracleCustomer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + id));

        String phoneNumber = telephoneNumberGenerator.generate();

        if (request.getPackageId() != null) {
            OraclePackage packagePlan = packageRepository.findById(request.getPackageId())
                    .orElseThrow(() -> new IllegalArgumentException("Package not found with ID: " + request.getPackageId()));

            // Mapper kullanılarak subscriber oluşturuluyor.
            OracleSubscriber subscriber = oracleSubscriberMapper.create(customer, packagePlan, request, phoneNumber);
            OracleSubscriber savedSubscriber = subscriberRepository.save(subscriber);

            // Balance da mapper ile oluşturuluyor.
            OracleBalance balance = oracleSubscriberMapper.createBalance(savedSubscriber, packagePlan);
            balanceRepository.save(balance);

            logger.info("Balance created successfully for subscriberId: {}", savedSubscriber.getId());
        } else {
            logger.warn("No packageId specified. Balance creation skipped.");
        }

        logger.info("Subscriber created successfully for userId: {}", id);
    }

    @Override
    public OracleSubscriber createSubscriber(Long id, SubscriberRequest request) {
        logger.info("Creating new subscriber for userId: {}", id);

        OracleCustomer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + id));

        String phoneNumber = telephoneNumberGenerator.generate();

        if (request.getPackageId() != null) {
            OraclePackage packagePlan = packageRepository.findById(request.getPackageId())
                    .orElseThrow(() -> new IllegalArgumentException("Package not found with ID: " + request.getPackageId()));

            OracleSubscriber subscriber = oracleSubscriberMapper.create(customer, packagePlan, request, phoneNumber);
            OracleSubscriber savedSubscriber = subscriberRepository.save(subscriber);

            OracleBalance balance = oracleSubscriberMapper.createBalance(savedSubscriber, packagePlan);
            balanceRepository.save(balance);

            List<OracleBalance> balances = new ArrayList<>();
            balances.add(balance);
            savedSubscriber.setBalances(balances);

            logger.info("Balance created successfully for subscriberId: {}", savedSubscriber.getId());
            logger.info("Subscriber created successfully for userId: {}", id);
            return savedSubscriber;
        } else {
            logger.warn("No packageId specified. Balance creation skipped.");
            return null;
        }
    }

    @Override
    public List<OracleSubscriber> getCustomerSubscribers(Long customerId) {
        if (customerId == null) {
            throw new IllegalArgumentException("Customer ID cannot be null");
        }

        List<OracleSubscriber> subscribers = subscriberRepository.findByCustomerId(customerId);
        return subscribers;
    }

    @Override
    public OracleSubscriber readById(Long id) {
        logger.info("Fetching subscriber by ID: {}", id);
        return findSubscriberById(id);
    }

    @Override
    public void update(SubscriberRequest subscriberRequest) {
        // Boş
    }

    @Override
    public void update(SubscriberUpdateRequest request) {
        logger.info("Updating subscriber with subscriberId: {}", request.getSubscriberId());

        OracleSubscriber subscriber = findSubscriberById(request.getSubscriberId());
        oracleSubscriberMapper.updateFromDto(request, subscriber);
        subscriberRepository.save(subscriber);

        logger.info("Subscriber updated successfully with subscriberId: {}", request.getSubscriberId());
    }

    @Override
    public void delete(Long id) {
        logger.info("Deleting subscriber for subscriberId: {}", id);

        OracleSubscriber subscriber = findSubscriberById(id);
        subscriberRepository.delete(subscriber);

        logger.info("Subscriber deleted successfully for subscriberId: {}", id);
    }

    private OracleSubscriber findSubscriberById(Long id) {
        return subscriberRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Subscriber not found with ID: {}", id);
                    return new IllegalArgumentException("Subscriber not found with ID: " + id);
                });
    }
}
