package com.ramobeko.accountordermanagement.model.entity;

import com.ramobeko.accountordermanagement.util.model.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@Entity
@Table(name = "t_customer", schema = "AOM")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Customer implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cust_id")
    private Long id;

    @Column(name = "cust_name", nullable = false)
    private String name;

    @Column(name = "cust_mail", nullable = false, unique = true)
    private String email;

    @Column(name = "cust_password", nullable = false)
    private String password;  // 🔑 Required for Spring Security

    @Column(name = "cust_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role; // 🎭 Enum for roles (USER, ADMIN)

    @Column(name = "cust_start_date")
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(name = "cust_address")
    private String address;

    @Column(name = "cust_status")
    private String status;

    // ✅ Spring Security Methods Implementation
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(role);
    }

    @Override
    public String getUsername() {
        return email; // 📧 Spring Security uses email as username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status.equalsIgnoreCase("ACTIVE");
    }
}