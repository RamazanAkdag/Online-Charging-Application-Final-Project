package com.ramobeko.accountordermanagement.model.dto;

import com.ramobeko.accountordermanagement.util.model.Role;

public class RegisterRequest implements IDTO {
    private String name;
    private String email;
    private String address;
    private String password;
    private Role role; // USER or ADMIN

    public RegisterRequest() {
    }

    public RegisterRequest(String name, String email, String address, String password, Role role) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.password = password;
        this.role = role;
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
}