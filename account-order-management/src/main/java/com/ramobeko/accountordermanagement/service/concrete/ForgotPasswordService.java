package com.ramobeko.accountordermanagement.service.concrete;

import com.ramobeko.accountordermanagement.model.entity.oracle.OracleCustomer;
import com.ramobeko.accountordermanagement.repository.oracle.OracleCustomerRepository;
import com.ramobeko.accountordermanagement.util.mail.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ForgotPasswordService {

    private final OracleCustomerRepository customerRepository;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.reset-password.base-url}") // ✨ base URL dışarıdan alınır
    private String resetPasswordBaseUrl;

    @Autowired
    public ForgotPasswordService(OracleCustomerRepository customerRepository,
                                 EmailSender emailSender,
                                 PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.emailSender = emailSender;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean processForgotPassword(String email) {
        Optional<OracleCustomer> optionalCustomer = customerRepository.findByEmail(email);

        if (optionalCustomer.isEmpty()) {
            return false;
        }

        OracleCustomer customer = optionalCustomer.get();

        String token = UUID.randomUUID().toString();
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(15);

        customer.setResetToken(token);
        customer.setResetTokenExpiration(expiration);
        customerRepository.save(customer);

        String resetLink = resetPasswordBaseUrl + "?token=" + token;

        String subject = "Şifre Sıfırlama Talebi";
        String body = "Merhaba " + customer.getName() + ",\n\n" +
                "Şifrenizi sıfırlamak için aşağıdaki bağlantıya tıklayın (15 dakika geçerli):\n" +
                resetLink + "\n\n" +
                "Bu isteği siz yapmadıysanız, lütfen bu e-postayı dikkate almayın.";

        return emailSender.send(email, subject, body);
    }

    public boolean resetPassword(String token, String newPassword) {
        OracleCustomer customer = customerRepository.findByResetToken(token);

        if (customer == null) return false;
        if (customer.getResetTokenExpiration() == null || customer.getResetTokenExpiration().isBefore(LocalDateTime.now())) {
            return false;
        }

        String encodedPassword = passwordEncoder.encode(newPassword);
        customer.setPassword(encodedPassword);
        customer.setResetToken(null);
        customer.setResetTokenExpiration(null);

        customerRepository.save(customer);
        return true;
    }
}
