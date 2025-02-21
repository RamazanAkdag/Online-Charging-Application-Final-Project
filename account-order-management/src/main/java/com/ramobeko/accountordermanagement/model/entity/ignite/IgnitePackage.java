package com.ramobeko.accountordermanagement.model.entity.ignite;


import org.apache.ignite.cache.query.annotations.QuerySqlField;
import java.io.Serializable;
import java.math.BigDecimal;

public class IgnitePackage implements Serializable {

    @QuerySqlField(index = true)
    private Long id;

    @QuerySqlField
    private String name;

    @QuerySqlField
    private Long amountMinutes;

    @QuerySqlField
    private Long amountSms;

    @QuerySqlField
    private Long amountData;

    @QuerySqlField
    private BigDecimal price;

    // Constructors
    public IgnitePackage() {
    }

    public IgnitePackage(Long id, String name, Long amountMinutes, Long amountSms, Long amountData, BigDecimal price) {
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
