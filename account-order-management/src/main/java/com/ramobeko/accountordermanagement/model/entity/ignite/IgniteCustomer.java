package com.ramobeko.accountordermanagement.model.entity.ignite;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ramobeko.accountordermanagement.util.model.Role;
import jakarta.persistence.*;
import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.apache.ignite.cache.query.annotations.QueryTextField;

import java.io.Serializable;

import java.util.Date;
@Entity
@Table(name = "t_customer")
public class IgniteCustomer implements Serializable {

    @Id
    @QuerySqlField(index = true)
    @Column(name = "cust_id")
    private Long id;

    @QuerySqlField
    @QueryTextField
    @Column(name = "cust_name", nullable = false)
    private String name;

    @QuerySqlField(index = true)
    @Column(name = "cust_mail", nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @QuerySqlField
    @Column(name = "cust_password", nullable = false)
    private String password;

    @QuerySqlField
    @Enumerated(EnumType.STRING)
    @Column(name = "cust_role", nullable = false)
    private Role role;

    @QuerySqlField
    @Temporal(TemporalType.DATE)
    @Column(name = "cust_start_date")
    private Date startDate;

    @QuerySqlField
    @Column(name = "cust_address")
    private String address;

    @QuerySqlField(index = true)
    @Column(name = "cust_status")
    private String status;

    // Getters and Setters

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

    public String getPassword() {
        return password;
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