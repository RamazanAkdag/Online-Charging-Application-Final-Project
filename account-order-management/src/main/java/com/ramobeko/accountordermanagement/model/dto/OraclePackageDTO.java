package com.ramobeko.accountordermanagement.model.dto;

import java.math.BigDecimal;

public class OraclePackageDTO implements IDTO {
    private Long id;
    private String name;
    private Long amountMinutes;
    private Long amountSms;
    private Long amountData;
    private BigDecimal price;

    // Constructors
    public OraclePackageDTO() {
    }

    public OraclePackageDTO(Long id, String name, Long amountMinutes, Long amountSms, Long amountData, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.amountMinutes = amountMinutes;
        this.amountSms = amountSms;
        this.amountData = amountData;
        this.price = price;
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

    public Long getAmountMinutes() {
        return amountMinutes;
    }

    public void setAmountMinutes(Long amountMinutes) {
        this.amountMinutes = amountMinutes;
    }

    public Long getAmountSms() {
        return amountSms;
    }

    public void setAmountSms(Long amountSms) {
        this.amountSms = amountSms;
    }

    public Long getAmountData() {
        return amountData;
    }

    public void setAmountData(Long amountData) {
        this.amountData = amountData;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
