package com.ramobeko.accountordermanagement.model.dto;

import com.ramobeko.accountordermanagement.model.entity.oracle.OracleCustomer;
import com.ramobeko.accountordermanagement.util.model.Role;

import java.util.Date;

public class OracleCustomerDTO implements IDTO {
    private Long id;
    private String name;
    private String email;
    private String address;
    private String status;
    private Role role;
    private Date startDate;

    // Constructors
    public OracleCustomerDTO() {}

    public OracleCustomerDTO(Long id, String name, String email, String address, String status, Role role, Date startDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.status = status;
        this.role = role;
        this.startDate = startDate; // 🛠️ Eklendi
    }

    // Entity'den DTO'ya dönüşüm için constructor
    public OracleCustomerDTO(OracleCustomer customer) {
        this.id = customer.getId();
        this.name = customer.getName();
        this.email = customer.getEmail();
        this.address = customer.getAddress();
        this.status = customer.getStatus();
        this.role = customer.getRole();
        this.startDate = customer.getStartDate(); // 🛠️ Eklendi
    }

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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Date getStartDate() { // 🛠️ Getter Eklendi
        return startDate;
    }

    public void setStartDate(Date startDate) { // 🛠️ Setter Eklendi
        this.startDate = startDate;
    }
}
