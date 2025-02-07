package com.ramobeko.accountordermanagement.model.dto;

import com.ramobeko.accountordermanagement.util.model.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String address;
    private String password;
    private Role role; // USER or ADMIN


}