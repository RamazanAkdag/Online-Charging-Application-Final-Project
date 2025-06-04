package com.ramobeko.accountordermanagement.service.concrete.ignite;

import com.ramobeko.accountordermanagement.model.shared.OracleSubscriber;
import com.ramobeko.accountordermanagement.repository.ignite.IgniteSubscriberRepository;
import com.ramobeko.accountordermanagement.util.mapper.ignite.OracleSubscriberToIgniteSubscriberMapper;
import com.ramobeko.ignite.IgniteSubscriber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IgniteSubscriberServiceTest {

    private IgniteSubscriberRepository repository;
    private OracleSubscriberToIgniteSubscriberMapper mapper;
    private IgniteSubscriberService igniteSubscriberService;

    @BeforeEach
    void setUp() {
        repository = mock(IgniteSubscriberRepository.class);
        mapper = mock(OracleSubscriberToIgniteSubscriberMapper.class);

        igniteSubscriberService = new IgniteSubscriberService(repository, mapper);
    }

    @Test
    void createFromOracle_shouldSaveSubscriber() {
        OracleSubscriber oracle = new OracleSubscriber();
        oracle.setId(1L);
        oracle.setPhoneNumber("905551234567");

        IgniteSubscriber ignite = new IgniteSubscriber();
        ignite.setId(1L);
        ignite.setPhoneNumber("905551234567");

        when(mapper.toIgniteWithBalances(oracle)).thenReturn(ignite);

        igniteSubscriberService.createFromOracle(oracle);

        verify(repository).save(Long.parseLong(ignite.getPhoneNumber()), ignite);
    }

    @Test
    void createFromOracle_shouldThrowException_whenOracleSubscriberIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                igniteSubscriberService.createFromOracle(null));
    }

    @Test
    void createFromOracle_shouldThrowException_whenOracleIdIsNull() {
        OracleSubscriber oracle = new OracleSubscriber(); // ID null
        assertThrows(IllegalArgumentException.class, () ->
                igniteSubscriberService.createFromOracle(oracle));
    }

    @Test
    void getById_shouldReturnSubscriber_whenExists() {
        IgniteSubscriber ignite = new IgniteSubscriber();
        ignite.setId(2L);

        when(repository.findById(2L)).thenReturn(Optional.of(ignite));

        IgniteSubscriber result = igniteSubscriberService.getById(2L);

        assertEquals(2L, result.getId());
    }

    @Test
    void getById_shouldThrowException_whenNotExists() {
        when(repository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                igniteSubscriberService.getById(3L));
    }

    @Test
    void getByPhoneNumber_shouldReturnSubscriber_whenExists() {
        IgniteSubscriber ignite = new IgniteSubscriber();
        ignite.setPhoneNumber("905551111111");

        when(repository.findByPhoneNumber("905551111111")).thenReturn(ignite);

        IgniteSubscriber result = igniteSubscriberService.getByPhoneNumber("905551111111");

        assertEquals("905551111111", result.getPhoneNumber());
    }

    @Test
    void getByPhoneNumber_shouldReturnNull_whenNotExists() {
        when(repository.findByPhoneNumber("905552222222")).thenReturn(null);

        IgniteSubscriber result = igniteSubscriberService.getByPhoneNumber("905552222222");

        assertNull(result);
    }

    @Test
    void getByPhoneNumber_shouldReturnNull_whenPhoneNumberIsNull() {
        IgniteSubscriber result = igniteSubscriberService.getByPhoneNumber(null);
        assertNull(result);
    }

    @Test
    void updateFromOracle_shouldUpdateSubscriber_whenExists() {
        OracleSubscriber oracle = new OracleSubscriber();
        oracle.setId(4L);
        oracle.setPhoneNumber("905553333333");

        IgniteSubscriber ignite = new IgniteSubscriber();
        ignite.setId(4L);
        ignite.setPhoneNumber("905553333333");

        when(repository.findById(4L)).thenReturn(Optional.of(ignite));
        when(mapper.toIgniteWithBalances(oracle)).thenReturn(ignite);

        igniteSubscriberService.updateFromOracle(oracle);

        verify(repository).save(ignite.getId(), ignite);
    }

    @Test
    void updateFromOracle_shouldThrowException_whenSubscriberNotExists() {
        OracleSubscriber oracle = new OracleSubscriber();
        oracle.setId(5L);
        oracle.setPhoneNumber("905554444444");

        when(repository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () ->
                igniteSubscriberService.updateFromOracle(oracle));
    }

    @Test
    void delete_shouldDeleteSubscriber_whenExists() {
        IgniteSubscriber ignite = new IgniteSubscriber();
        ignite.setId(6L);

        when(repository.findById(6L)).thenReturn(Optional.of(ignite));

        igniteSubscriberService.delete(6L);

        verify(repository).deleteById(6L);
    }

    @Test
    void delete_shouldThrowException_whenSubscriberNotExists() {
        when(repository.findById(7L)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () ->
                igniteSubscriberService.delete(7L));
    }
}