package com.ramobeko.accountordermanagement.service.concrete;

import com.ramobeko.accountordermanagement.model.entity.oracle.OracleCustomer;
import com.ramobeko.accountordermanagement.repository.oracle.OracleCustomerRepository;
import com.ramobeko.accountordermanagement.util.mail.EmailSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ForgotPasswordServiceTest {

    private OracleCustomerRepository customerRepository;
    private EmailSender emailSender;
    private PasswordEncoder passwordEncoder;

    private ForgotPasswordService forgotPasswordService;

    @BeforeEach
    void setUp() throws Exception {
        customerRepository = mock(OracleCustomerRepository.class);
        emailSender = mock(EmailSender.class);
        passwordEncoder = mock(PasswordEncoder.class);

        forgotPasswordService = new ForgotPasswordService(customerRepository, emailSender, passwordEncoder);

        // resetPasswordBaseUrl değerini manuel set etmemiz gerekiyor (çünkü @Value Spring injection yapılmaz testte)
        var field = ForgotPasswordService.class.getDeclaredField("resetPasswordBaseUrl");
        field.setAccessible(true);
        field.set(forgotPasswordService, "http://localhost/reset");
    }

    // --- processForgotPassword() TESTLERİ ---

    @Test
    void processForgotPassword_shouldSendEmail_whenCustomerExists() {
        OracleCustomer customer = new OracleCustomer();
        customer.setEmail("user@example.com");
        customer.setName("Test User");

        when(customerRepository.findByEmail("user@example.com")).thenReturn(Optional.of(customer));
        when(emailSender.send(anyString(), anyString(), anyString())).thenReturn(true);

        boolean result = forgotPasswordService.processForgotPassword("user@example.com");

        assertTrue(result);
        assertNotNull(customer.getResetToken());
        assertNotNull(customer.getResetTokenExpiration());
        verify(customerRepository).save(customer);
        verify(emailSender).send(eq("user@example.com"), anyString(), contains("reset"));
    }

    @Test
    void processForgotPassword_shouldReturnFalse_whenCustomerNotFound() {
        when(customerRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        boolean result = forgotPasswordService.processForgotPassword("missing@example.com");

        assertFalse(result);
    }

    // --- resetPassword() TESTLERİ ---

    @Test
    void resetPassword_shouldChangePassword_whenTokenIsValid() {
        OracleCustomer customer = new OracleCustomer();
        customer.setResetToken("valid-token");
        customer.setResetTokenExpiration(LocalDateTime.now().plusMinutes(10));

        when(customerRepository.findByResetToken("valid-token")).thenReturn(customer);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedPassword");

        boolean result = forgotPasswordService.resetPassword("valid-token", "newPassword");

        assertTrue(result);
        assertEquals("encodedPassword", customer.getPassword());
        assertNull(customer.getResetToken());
        assertNull(customer.getResetTokenExpiration());
        verify(customerRepository).save(customer);
    }

    @Test
    void resetPassword_shouldReturnFalse_whenTokenNotFound() {
        when(customerRepository.findByResetToken("unknown-token")).thenReturn(null);

        boolean result = forgotPasswordService.resetPassword("unknown-token", "password");

        assertFalse(result);
    }

    @Test
    void resetPassword_shouldReturnFalse_whenTokenExpired() {
        OracleCustomer customer = new OracleCustomer();
        customer.setResetToken("expired-token");
        customer.setResetTokenExpiration(LocalDateTime.now().minusMinutes(1)); // süresi geçmiş

        when(customerRepository.findByResetToken("expired-token")).thenReturn(customer);

        boolean result = forgotPasswordService.resetPassword("expired-token", "password");

        assertFalse(result);
    }
}

