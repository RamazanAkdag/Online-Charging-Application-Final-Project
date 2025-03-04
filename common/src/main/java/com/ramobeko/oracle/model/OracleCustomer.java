package com.ramobeko.oracle.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ramobeko.oracle.util.Role;
import jakarta.persistence.*;


import java.util.Date;

@Entity
@Table(name = "t_customer", schema = "AOM")
public class OracleCustomer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cust_id")
    private Long id;

    @Column(name = "cust_name", nullable = false)
    private String name;

    @Column(name = "cust_mail", nullable = false, unique = true)
    private String email;

    @JsonIgnore
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