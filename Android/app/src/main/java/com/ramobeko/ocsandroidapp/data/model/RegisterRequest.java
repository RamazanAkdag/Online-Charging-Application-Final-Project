package com.ramobeko.ocsandroidapp.data.model;

public class RegisterRequest {
    private String name;
    private String email;
    private String address;
    private String password;

    public RegisterRequest(String name, String email, String address, String password) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPassword() {
        return password;
    }
}
