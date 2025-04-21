package com.ramobeko.ocsandroidapp.data.model.auth;

public class LoginRequest {
    private String email;
    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters (optional)
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}

