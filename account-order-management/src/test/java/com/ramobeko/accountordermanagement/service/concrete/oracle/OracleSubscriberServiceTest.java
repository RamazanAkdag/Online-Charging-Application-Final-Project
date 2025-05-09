package com.ramobeko.accountordermanagement.service.concrete.oracle;

import com.ramobeko.accountordermanagement.model.dto.request.SubscriberRequest;
import com.ramobeko.accountordermanagement.model.dto.request.SubscriberUpdateRequest;
import com.ramobeko.accountordermanagement.model.entity.oracle.*;
import com.ramobeko.accountordermanagement.model.shared.OracleSubscriber;
import com.ramobeko.accountordermanagement.repository.oracle.*;
import com.ramobeko.accountordermanagement.util.generator.ITelephoneNumberGenerator;
import com.ramobeko.accountordermanagement.util.mapper.oracle.OracleSubscriberMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OracleSubscriberServiceTest {

    private OracleSubscriberRepository subscriberRepository;
    private OracleCustomerRepository customerRepository;
    private OracleBalanceRepository balanceRepository;
    private OraclePackageRepository packageRepository;
    private ITelephoneNumberGenerator telephoneNumberGenerator;
    private OracleSubscriberMapper mapper;

    private OracleSubscriberService service;

    @BeforeEach
    void setUp() {
        subscriberRepository = mock(OracleSubscriberRepository.class);
        customerRepository = mock(OracleCustomerRepository.class);
        balanceRepository = mock(OracleBalanceRepository.class);
        packageRepository = mock(OraclePackageRepository.class);
        telephoneNumberGenerator = mock(ITelephoneNumberGenerator.class);
        mapper = mock(OracleSubscriberMapper.class);

        service = new OracleSubscriberService(
                subscriberRepository,
                customerRepository,
                balanceRepository,
                packageRepository,
                telephoneNumberGenerator,
                mapper
        );
    }

    @Test
    void readAll_shouldReturnSubscriberList() {
        when(subscriberRepository.findAll()).thenReturn(List.of(new OracleSubscriber()));

        assertFalse(service.readAll().isEmpty());
    }

    @Test
    void create_shouldCreateSubscriberAndBalance_whenPackageExists() {
        SubscriberRequest request = new SubscriberRequest();
        request.setPackageId(10L);

        OracleCustomer customer = new OracleCustomer();
        customer.setId(1L);

        OraclePackage oraclePackage = new OraclePackage();
        oraclePackage.setId(10L);

        OracleSubscriber subscriber = new OracleSubscriber();
        subscriber.setId(100L);

        OracleBalance balance = new OracleBalance();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(packageRepository.findById(10L)).thenReturn(Optional.of(oraclePackage));
        when(telephoneNumberGenerator.generate()).thenReturn("905551234567");
        when(mapper.create(customer, oraclePackage, request, "905551234567")).thenReturn(subscriber);
        when(subscriberRepository.save(subscriber)).thenReturn(subscriber);
        when(mapper.createBalance(subscriber, oraclePackage)).thenReturn(balance);

        service.create(1L, request);

        verify(balanceRepository).save(balance);
    }

    @Test
    void create_shouldThrowException_whenCustomerNotFound() {
        SubscriberRequest request = new SubscriberRequest();
        when(customerRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                service.create(2L, request));
    }

    @Test
    void createSubscriber_shouldReturnSubscriber_whenPackageExists() {
        SubscriberRequest request = new SubscriberRequest();
        request.setPackageId(20L);

        OracleCustomer customer = new OracleCustomer();
        customer.setId(2L);

        OraclePackage oraclePackage = new OraclePackage();
        oraclePackage.setId(20L);

        OracleSubscriber subscriber = new OracleSubscriber();
        subscriber.setId(200L);

        OracleBalance balance = new OracleBalance();

        when(customerRepository.findById(2L)).thenReturn(Optional.of(customer));
        when(packageRepository.findById(20L)).thenReturn(Optional.of(oraclePackage));
        when(telephoneNumberGenerator.generate()).thenReturn("905559876543");
        when(mapper.create(customer, oraclePackage, request, "905559876543")).thenReturn(subscriber);
        when(subscriberRepository.save(subscriber)).thenReturn(subscriber);
        when(mapper.createBalance(subscriber, oraclePackage)).thenReturn(balance);

        OracleSubscriber result = service.createSubscriber(2L, request);

        assertNotNull(result);
        assertEquals(200L, result.getId());
        verify(balanceRepository).save(balance);
    }

    @Test
    void createSubscriber_shouldReturnNull_whenNoPackageId() {
        SubscriberRequest request = new SubscriberRequest(); // packageId null

        OracleCustomer customer = new OracleCustomer();
        customer.setId(3L);

        when(customerRepository.findById(3L)).thenReturn(Optional.of(customer));
        when(telephoneNumberGenerator.generate()).thenReturn("905557777777");

        OracleSubscriber result = service.createSubscriber(3L, request);

        assertNull(result);
    }

    @Test
    void getCustomerSubscribers_shouldReturnList() {
        OracleSubscriber subscriber = new OracleSubscriber();
        subscriber.setId(300L);

        when(subscriberRepository.findByCustomerId(4L)).thenReturn(List.of(subscriber));

        List<OracleSubscriber> result = service.getCustomerSubscribers(4L);

        assertFalse(result.isEmpty());
    }

    @Test
    void getCustomerSubscribers_shouldThrowException_whenCustomerIdIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                service.getCustomerSubscribers(null));
    }

    @Test
    void readById_shouldReturnSubscriber_whenExists() {
        OracleSubscriber subscriber = new OracleSubscriber();
        subscriber.setId(400L);

        when(subscriberRepository.findById(400L)).thenReturn(Optional.of(subscriber));

        OracleSubscriber result = service.readById(400L);

        assertEquals(400L, result.getId());
    }

    @Test
    void readById_shouldThrowException_whenSubscriberNotFound() {
        when(subscriberRepository.findById(401L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                service.readById(401L));
    }

    @Test
    void update_shouldUpdateSubscriber_whenExists() {
        SubscriberUpdateRequest request = new SubscriberUpdateRequest();
        request.setSubscriberId(500L);

        OracleSubscriber subscriber = new OracleSubscriber();
        subscriber.setId(500L);

        when(subscriberRepository.findById(500L)).thenReturn(Optional.of(subscriber));

        service.update(request);

        verify(mapper).updateFromDto(request, subscriber);
        verify(subscriberRepository).save(subscriber);
    }

    @Test
    void update_shouldThrowException_whenSubscriberNotFound() {
        SubscriberUpdateRequest request = new SubscriberUpdateRequest();
        request.setSubscriberId(501L);

        when(subscriberRepository.findById(501L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                service.update(request));
    }

    @Test
    void delete_shouldDeleteSubscriber_whenExists() {
        OracleSubscriber subscriber = new OracleSubscriber();
        subscriber.setId(600L);

        when(subscriberRepository.findById(600L)).thenReturn(Optional.of(subscriber));

        service.delete(600L);

        verify(subscriberRepository).delete(subscriber);
    }

    @Test
    void delete_shouldThrowException_whenSubscriberNotFound() {
        when(subscriberRepository.findById(601L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                service.delete(601L));
    }
}
