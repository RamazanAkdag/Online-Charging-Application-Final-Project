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
    private final OracleBalanceRepository balanceRepository;
    private final OraclePackageRepository packageRepository;
    private final ITelephoneNumberGenerator telephoneNumberGenerator; // 📌 Telefon numarası üreten servis

    public OracleSubscriberService(OracleSubscriberRepository subscriberRepository,
                                   OracleCustomerRepository customerRepository,
                                   OracleBalanceRepository balanceRepository,
                                   OraclePackageRepository packageRepository,
                                   ITelephoneNumberGenerator telephoneNumberGenerator) { // 📌 Yeni bağımlılık eklendi
        this.subscriberRepository = subscriberRepository;
        this.customerRepository = customerRepository;
        this.balanceRepository = balanceRepository;
        this.packageRepository = packageRepository;
        this.telephoneNumberGenerator = telephoneNumberGenerator;
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

        // 📌 Telefon numarası artık TelephoneNumberGenerator kullanılarak oluşturuluyor
        String phoneNumber = telephoneNumberGenerator.generate();
        if (request.getPackageId() != null) {
            // Paket bilgisi varsa, balance oluştur

            OraclePackage packagePlan = packageRepository.findById(request.getPackageId())
                    .orElseThrow(() -> new IllegalArgumentException("Package not found with ID: " + request.getPackageId()));
            OracleSubscriber subscriber = new OracleSubscriber();
            subscriber.setCustomer(customer);
            subscriber.setPackagePlan(packagePlan);
            subscriber.setPhoneNumber(phoneNumber);
            subscriber.setStartDate(request.getStartDate() != null ? request.getStartDate() : new Date());
            subscriber.setStatus("ACTIVE");

            OracleSubscriber savedSubscriber = subscriberRepository.save(subscriber);



            OracleBalance balance = new OracleBalance();
            balance.setSubscriber(savedSubscriber);
            balance.setPackagePlan(packagePlan);
            balance.setLevelMinutes(packagePlan.getAmountMinutes());
            balance.setLevelSms(packagePlan.getAmountSms());
            balance.setLevelData(packagePlan.getAmountData());
            balance.setStartDate(new Date());
            balance.setEndDate(null);

            balanceRepository.save(balance);
            logger.info("Balance created successfully for subscriberId: {}", savedSubscriber.getId());
        } else {
            logger.warn("No packageId specified. Balance creation skipped.");
        }

        logger.info("Subscriber created successfully for userId: {}", id);
    }

    @Override
    public OracleSubscriber readById(Long id) {
        logger.info("Fetching subscriber by ID: {}", id);
        return findSubscriberById(id);
    }

    @Override
    // This method is not used because updates are handled using SubscriberUpdateRequest.
    // The system does not use SubscriberRequest for updating subscribers.
    public void update(SubscriberRequest subscriberRequest) {
        // Unused method: The system uses SubscriberUpdateRequest instead.
    }

    @Override
    public void update(SubscriberUpdateRequest request) {
        logger.info("Updating subscriber with subscriberId: {}", request.getSubscriberId());

        OracleSubscriber subscriber = findSubscriberById(request.getSubscriberId());

        subscriber.setStartDate(request.getStartDate());
        subscriber.setEndDate(request.getEndDate());
        subscriber.setStatus(request.getStatus());

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
