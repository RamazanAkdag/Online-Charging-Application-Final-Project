package com.ramobeko.accountordermanagement.model.entity.hazelcast;

import java.io.Serializable;
import java.util.Date;

public class HazelcastCustomer implements Serializable {

    private Long id;
    private String name;
    private String email;
    private String role;
    private Date startDate;
    private String address;
    private String status;

    // Default Constructor
    public HazelcastCustomer() {}

    public HazelcastCustomer(Long id, String name, String email, String role, Date startDate, String address, String status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.startDate = startDate;
        this.address = address;
        this.status = status;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

