package com.ramobeko.accountordermanagement.service.concrete.oracle;

import com.ramobeko.accountordermanagement.model.dto.OracleCustomerDTO;
import com.ramobeko.accountordermanagement.model.dto.RegisterRequest;
import com.ramobeko.accountordermanagement.model.dto.request.AuthRequest;
import com.ramobeko.accountordermanagement.model.dto.request.ChangePasswordRequest;
import com.ramobeko.accountordermanagement.model.entity.oracle.OracleCustomer;
import com.ramobeko.accountordermanagement.repository.oracle.OracleCustomerRepository;
import com.ramobeko.accountordermanagement.security.JwtUtil;
import com.ramobeko.accountordermanagement.util.mapper.dto.DtoToOracleCustomerMapper;
import com.ramobeko.accountordermanagement.util.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OracleCustomerServiceTest {

    private OracleCustomerRepository repository;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;
    private DtoToOracleCustomerMapper mapper;

    private OracleCustomerService service;

    @BeforeEach
    void setUp() {
        repository = mock(OracleCustomerRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        jwtUtil = mock(JwtUtil.class);
        mapper = mock(DtoToOracleCustomerMapper.class);

        service = new OracleCustomerService(repository, passwordEncoder, jwtUtil, mapper);
    }

    @Test
    void authenticateCustomer_shouldReturnToken_whenCredentialsAreCorrect() {
        AuthRequest request = new AuthRequest("test@example.com", "password");

        OracleCustomer customer = new OracleCustomer();
        customer.setId(1L);
        customer.setEmail("test@example.com");
        customer.setPassword("hashedPassword");
        customer.setRole(Role.USER); // EKLENDİ

        when(repository.findByEmail("test@example.com")).thenReturn(Optional.of(customer));
        when(passwordEncoder.matches("password", "hashedPassword")).thenReturn(true);
        when(jwtUtil.generateToken(1L, "test@example.com", "USER")).thenReturn("jwt-token");

        String token = service.authenticateCustomer(request);

        assertEquals("jwt-token", token);
    }

    @Test
    void authenticateCustomer_shouldThrowException_whenPasswordIsWrong() {
        AuthRequest request = new AuthRequest("test@example.com", "wrongpassword");

        OracleCustomer customer = new OracleCustomer();
        customer.setPassword("hashedPassword");

        when(repository.findByEmail("test@example.com")).thenReturn(Optional.of(customer));
        when(passwordEncoder.matches("wrongpassword", "hashedPassword")).thenReturn(false);

        assertThrows(BadCredentialsException.class, () ->
                service.authenticateCustomer(request));
    }

    @Test
    void register_shouldSaveCustomer_whenEmailIsUnique() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("new@example.com");

        OracleCustomer customer = new OracleCustomer();
        customer.setEmail("new@example.com");

        when(repository.existsByEmail("new@example.com")).thenReturn(false);
        when(mapper.fromRegisterRequest(request, passwordEncoder)).thenReturn(customer);
        when(repository.save(customer)).thenReturn(customer);

        OracleCustomer result = service.register(request);

        assertEquals("new@example.com", result.getEmail());
    }

    @Test
    void register_shouldThrowException_whenEmailExists() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("exists@example.com");

        when(repository.existsByEmail("exists@example.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () ->
                service.register(request));
    }

    @Test
    void readById_shouldReturnCustomer_whenExists() {
        OracleCustomer customer = new OracleCustomer();
        customer.setId(2L);

        when(repository.findById(2L)).thenReturn(Optional.of(customer));

        OracleCustomer result = service.readById(2L);

        assertEquals(2L, result.getId());
    }

    @Test
    void readById_shouldThrowException_whenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                service.readById(99L));
    }

    @Test
    void update_shouldUpdateCustomer_whenExists() {
        OracleCustomerDTO dto = new OracleCustomerDTO();
        dto.setId(3L);

        OracleCustomer existing = new OracleCustomer();
        existing.setId(3L);

        when(repository.findById(3L)).thenReturn(Optional.of(existing));

        service.update(dto);

        verify(mapper).updateFromDto(dto, existing);
        verify(repository).save(existing);
    }

    @Test
    void update_shouldThrowException_whenCustomerNotFound() {
        OracleCustomerDTO dto = new OracleCustomerDTO();
        dto.setId(4L);

        when(repository.findById(4L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                service.update(dto));
    }

    @Test
    void delete_shouldRemoveCustomer_whenExists() {
        when(repository.existsById(5L)).thenReturn(true);

        service.delete(5L);

        verify(repository).deleteById(5L);
    }

    @Test
    void delete_shouldThrowException_whenCustomerNotFound() {
        when(repository.existsById(6L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () ->
                service.delete(6L));
    }

    @Test
    void changePassword_shouldUpdatePassword_whenOldPasswordMatches() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setEmail("test@example.com");
        request.setOldPassword("old");
        request.setNewPassword("new");

        OracleCustomer customer = new OracleCustomer();
        customer.setPassword("hashedOld");

        when(repository.findByEmail("test@example.com")).thenReturn(Optional.of(customer));
        when(passwordEncoder.matches("old", "hashedOld")).thenReturn(true);
        when(passwordEncoder.encode("new")).thenReturn("hashedNew");

        service.changePassword(request);

        assertEquals("hashedNew", customer.getPassword());
        verify(repository).save(customer);
    }

    @Test
    void changePassword_shouldThrowException_whenOldPasswordDoesNotMatch() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setEmail("test@example.com");
        request.setOldPassword("wrong");

        OracleCustomer customer = new OracleCustomer();
        customer.setPassword("hashedOld");

        when(repository.findByEmail("test@example.com")).thenReturn(Optional.of(customer));
        when(passwordEncoder.matches("wrong", "hashedOld")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () ->
                service.changePassword(request));
    }

    @Test
    void getCustomerDetails_shouldReturnCustomer_whenExists() {
        OracleCustomer customer = new OracleCustomer();
        customer.setEmail("detail@example.com");

        when(repository.findByEmail("detail@example.com")).thenReturn(Optional.of(customer));

        OracleCustomer result = service.getCustomerDetails("detail@example.com");

        assertEquals("detail@example.com", result.getEmail());
    }

    @Test
    void getCustomerDetails_shouldThrowException_whenCustomerNotFound() {
        when(repository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                service.getCustomerDetails("missing@example.com"));
    }
}

