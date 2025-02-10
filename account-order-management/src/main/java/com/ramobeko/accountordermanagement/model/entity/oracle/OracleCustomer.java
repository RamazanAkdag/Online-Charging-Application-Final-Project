package com.ramobeko.accountordermanagement.model.entity.oracle;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ramobeko.accountordermanagement.util.model.Role;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.apache.ignite.cache.query.annotations.QueryTextField;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "t_customer", schema = "AOM")
public class OracleCustomer implements UserDetails, Serializable {

    @Id
    @SequenceGenerator(name = "cust_seq_gen", sequenceName = "AOM.CUST_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cust_seq_gen")
    @Column(name = "cust_id")
    private Long id;
    @Column(name = "cust_name", nullable = false)
    private String name;

    @Column(name = "cust_mail", nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(name = "cust_password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "cust_role", nullable = false)
    private Role role;

    @Temporal(TemporalType.DATE)
    @Column(name = "cust_start_date")
    private Date startDate;

    @Column(name = "cust_address")
    private String address;

    @Column(name = "cust_status")
    private String status;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(role);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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

    // ✅ Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}