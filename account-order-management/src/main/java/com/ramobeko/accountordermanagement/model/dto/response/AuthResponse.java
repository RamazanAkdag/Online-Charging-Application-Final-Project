package com.ramobeko.accountordermanagement.model.dto.response;


import com.ramobeko.accountordermanagement.model.dto.IDTO;

public class AuthResponse implements IDTO {
    private String token;

    public AuthResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
