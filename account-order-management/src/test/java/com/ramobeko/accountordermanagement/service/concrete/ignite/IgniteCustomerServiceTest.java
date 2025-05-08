package com.ramobeko.accountordermanagement.service.concrete.ignite;

import com.ramobeko.accountordermanagement.model.entity.ignite.IgniteCustomer;
import com.ramobeko.accountordermanagement.repository.ignite.IgniteCustomerRepository;
import com.ramobeko.accountordermanagement.util.helper.IgniteCustomerInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IgniteCustomerServiceTest {

    private IgniteCustomerRepository igniteCustomerRepository;
    private IgniteCustomerInitializer igniteCustomerInitializer;
    private PasswordEncoder passwordEncoder;
    private IgniteCustomerService igniteCustomerService;

    @BeforeEach
    void setUp() {
        igniteCustomerRepository = mock(IgniteCustomerRepository.class);
        igniteCustomerInitializer = mock(IgniteCustomerInitializer.class);
        passwordEncoder = mock(PasswordEncoder.class); // şu an kullanılmıyor ama constructor istiyor

        igniteCustomerService = new IgniteCustomerService(
                igniteCustomerRepository,
                passwordEncoder,
                igniteCustomerInitializer
        );
    }

    @Test
    void register_shouldInitializeAndSaveCustomer() {
        IgniteCustomer customer = new IgniteCustomer();
        customer.setId(1L);
        customer.setEmail("test@example.com");

        when(igniteCustomerRepository.save(1L, customer)).thenReturn(customer);

        IgniteCustomer saved = igniteCustomerService.register(customer);

        verify(igniteCustomerInitializer).initializeCustomer(customer);
        verify(igniteCustomerRepository).save(1L, customer);
        assertEquals("test@example.com", saved.getEmail());
    }

    @Test
    void create_shouldSaveCustomer() {
        IgniteCustomer customer = new IgniteCustomer();
        customer.setEmail("create@example.com");

        igniteCustomerService.create(customer);

        verify(igniteCustomerRepository).save(customer);
    }

    @Test
    void update_shouldSaveCustomer_whenExists() {
        IgniteCustomer customer = new IgniteCustomer();
        customer.setId(2L);
        customer.setEmail("update@example.com");

        when(igniteCustomerRepository.existsById(2L)).thenReturn(true);

        igniteCustomerService.update(customer);

        verify(igniteCustomerRepository).save(customer);
    }

    @Test
    void update_shouldThrowException_whenCustomerDoesNotExist() {
        IgniteCustomer customer = new IgniteCustomer();
        customer.setId(3L);

        when(igniteCustomerRepository.existsById(3L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                igniteCustomerService.update(customer));

        assertEquals("Customer not found with ID: 3", exception.getMessage());

        verify(igniteCustomerRepository, never()).save(any());
    }

    @Test
    void delete_shouldCallDeleteById() {
        igniteCustomerService.delete(4L);

        verify(igniteCustomerRepository).deleteById(4L);
    }
}
