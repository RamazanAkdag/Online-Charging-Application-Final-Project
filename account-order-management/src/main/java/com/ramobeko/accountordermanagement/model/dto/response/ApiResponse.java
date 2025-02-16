package com.ramobeko.accountordermanagement.model.dto.response;

import com.ramobeko.accountordermanagement.model.dto.IDTO;

public class ApiResponse implements IDTO {
    private String message;

    public ApiResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}