package com.ramobeko.accountordermanagement.util.mapper.oracle;

import com.ramobeko.accountordermanagement.model.dto.OracleCustomerDTO;
import com.ramobeko.accountordermanagement.model.dto.RegisterRequest;
import com.ramobeko.accountordermanagement.model.entity.oracle.OracleCustomer;
import com.ramobeko.accountordermanagement.util.model.Role;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

public class OracleCustomerMapper {

    /**
     * RegisterRequest'ten OracleCustomer entity'si oluşturur.
     *
     * @param request RegisterRequest nesnesi
     * @param passwordEncoder Parola şifreleme işlemi için PasswordEncoder
     * @return Oluşturulmuş OracleCustomer entity'si
     */
    public static OracleCustomer fromRegisterRequest(RegisterRequest request, PasswordEncoder passwordEncoder) {
        OracleCustomer customer = new OracleCustomer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setAddress(request.getAddress());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setStartDate(new Date());
        customer.setStatus("ACTIVE");
        customer.setRole(request.getRole() != null ? request.getRole() : Role.USER);
        return customer;
    }

    /**
     * OracleCustomerDTO kullanılarak var olan OracleCustomer nesnesini günceller.
     *
     * @param dto Güncelleme bilgilerini içeren DTO
     * @param existing Güncellenmek istenen mevcut OracleCustomer nesnesi
     * @return Güncellenmiş OracleCustomer nesnesi
     */
    public static OracleCustomer updateFromDTO(OracleCustomerDTO dto, OracleCustomer existing) {
        if (dto.getName() != null) {
            existing.setName(dto.getName());
        }
        if (dto.getAddress() != null) {
            existing.setAddress(dto.getAddress());
        }
        if (dto.getStatus() != null) {
            existing.setStatus(dto.getStatus());
        }
        // Diğer alanları da ihtiyaç varsa ekleyebilirsiniz.
        return existing;
    }

    /**
     * OracleCustomer nesnesini OracleCustomerDTO'ya dönüştürür.
     *
     * @param customer OracleCustomer nesnesi
     * @return Dönüştürülmüş OracleCustomerDTO nesnesi
     */
    public static OracleCustomerDTO toDTO(OracleCustomer customer) {
        OracleCustomerDTO dto = new OracleCustomerDTO();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setEmail(customer.getEmail());
        dto.setAddress(customer.getAddress());
        dto.setStatus(customer.getStatus());
        // Diğer gerekli alanları ekleyebilirsiniz.
        return dto;
    }
}
