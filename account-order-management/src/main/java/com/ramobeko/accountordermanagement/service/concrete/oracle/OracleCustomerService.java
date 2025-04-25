package com.ramobeko.accountordermanagement.service.concrete.oracle;

import com.ramobeko.accountordermanagement.model.dto.OracleCustomerDTO;
import com.ramobeko.accountordermanagement.model.dto.RegisterRequest;
import com.ramobeko.accountordermanagement.model.dto.request.AuthRequest;
import com.ramobeko.accountordermanagement.model.dto.request.ChangePasswordRequest;
import com.ramobeko.accountordermanagement.model.entity.oracle.OracleCustomer;
import com.ramobeko.accountordermanagement.repository.oracle.OracleCustomerRepository;
import com.ramobeko.accountordermanagement.security.JwtUtil;
import com.ramobeko.accountordermanagement.service.abstrct.oracle.IOracleCustomerService;
import com.ramobeko.accountordermanagement.util.mapper.dto.CustomerMapper;
import com.ramobeko.accountordermanagement.util.mapper.request.RegisterRequestMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OracleCustomerService implements IOracleCustomerService {

    private static final Logger logger = LogManager.getLogger(OracleCustomerService.class);

    private final OracleCustomerRepository oracleCustomerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public OracleCustomerService(OracleCustomerRepository oracleCustomerRepository,
                                 PasswordEncoder passwordEncoder,
                                 JwtUtil jwtUtil) {
        this.oracleCustomerRepository = oracleCustomerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public String authenticateCustomer(AuthRequest request) {
        logger.info("🔐 [authenticateCustomer] Kullanıcı doğrulama: {}", request.getEmail());

        OracleCustomer customer = findCustomerByEmail(request.getEmail());

        if (!passwordEncoder.matches(request.getPassword(), customer.getPassword())) {
            logger.error("❌ [authenticateCustomer] Doğrulama başarısız: {}", request.getEmail());
            throw new BadCredentialsException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(customer.getId(), customer.getEmail(), customer.getRole().name());
        logger.info("✅ [authenticateCustomer] Doğrulama başarılı: {}", request.getEmail());
        return token;
    }

    @Override
    public void create(Long id, OracleCustomerDTO oracleCustomerDTO) {
        // Gereksizse boş bırakıldı
    }

    @Override
    public OracleCustomer register(RegisterRequest request) {
        logger.info("📝 [register] Yeni kullanıcı kaydı: {}", request.getEmail());

        if (oracleCustomerRepository.existsByEmail(request.getEmail())) {
            logger.warn("⚠️ [register] E-posta zaten kullanımda: {}", request.getEmail());
            throw new IllegalArgumentException("Email already exists");
        }

        OracleCustomer customer = RegisterRequestMapper.toEntity(request, passwordEncoder);
        OracleCustomer savedCustomer = oracleCustomerRepository.save(customer);
        logger.info("🎉 [register] Kullanıcı başarıyla kaydedildi: {}", savedCustomer.getEmail());

        return savedCustomer;
    }

    @Override
    public OracleCustomer readById(Long id) {
        logger.info("🔍 [readById] Kullanıcı ID ile getiriliyor: {}", id);
        return findCustomerById(id);
    }

    @Override
    public List<OracleCustomer> readAll() {
        logger.info("📄 [readAll] Tüm kullanıcılar getiriliyor.");
        return oracleCustomerRepository.findAll();
    }

    @Override
    public void update(OracleCustomerDTO oracleCustomerDTO) {
        logger.info("🔄 [update] Kullanıcı güncelleniyor: {}", oracleCustomerDTO.getId());

        OracleCustomer existingCustomer = findCustomerById(oracleCustomerDTO.getId());
        OracleCustomer updatedCustomer = CustomerMapper.updateFromDTO(oracleCustomerDTO, existingCustomer);
        oracleCustomerRepository.save(updatedCustomer);
        logger.info("✅ [update] Kullanıcı güncellendi: {}", updatedCustomer.getId());
    }

    @Override
    public void delete(Long id) {
        logger.info("🗑️ [delete] Kullanıcı siliniyor: {}", id);

        if (!oracleCustomerRepository.existsById(id)) {
            logger.error("❌ [delete] Kullanıcı bulunamadı: {}", id);
            throw new IllegalArgumentException("Customer not found");
        }

        oracleCustomerRepository.deleteById(id);
        logger.info("✅ [delete] Kullanıcı silindi: {}", id);
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        logger.info("🔑 [changePassword] Kullanıcı şifresi değiştiriliyor: {}", request.getEmail());

        OracleCustomer customer = findCustomerByEmail(request.getEmail());

        if (!passwordEncoder.matches(request.getOldPassword(), customer.getPassword())) {
            logger.error("❌ [changePassword] Eski şifre hatalı: {}", request.getEmail());
            throw new IllegalArgumentException("Invalid old password");
        }

        customer.setPassword(passwordEncoder.encode(request.getNewPassword()));
        oracleCustomerRepository.save(customer);
        logger.info("✅ [changePassword] Şifre başarıyla değiştirildi: {}", request.getEmail());
    }

    @Override
    public OracleCustomer getCustomerDetails(String email) {
        logger.info("📌 [getCustomerDetails] Kullanıcı detayları alınıyor: {}", email);
        return findCustomerByEmail(email);
    }

    public OracleCustomer findCustomerByEmail(String email) {
        return oracleCustomerRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("❌ [findCustomerByEmail] Kullanıcı bulunamadı: {}", email);
                    return new IllegalArgumentException("Customer not found with email: " + email);
                });
    }

    private OracleCustomer findCustomerById(Long id) {
        return oracleCustomerRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("❌ [findCustomerById] Kullanıcı bulunamadı: {}", id);
                    return new IllegalArgumentException("Customer not found with ID: " + id);
                });
    }
}
