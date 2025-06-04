package com.ramobeko.accountordermanagement.model.entity.oracle;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "t_package", schema = "aom")
public class OraclePackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pkg_id", nullable = false)
    private Long id;

    @Column(name = "pkg_name", length = 100, nullable = false)
    private String name;

    @Column(name = "pkg_amount_min", nullable = true)
    private Long amountMinutes;

    @Column(name = "pkg_amount_sms", nullable = true)
    private Long amountSms;

    @Column(name = "pkg_amount_data", nullable = true)
    private Long amountData;

    @Column(name = "pkg_price", precision = 10, scale = 2, nullable = true)
    private BigDecimal price;

    // Constructors
    public OraclePackage() {
    }

    public OraclePackage(String name, Long amountMinutes, Long amountSms, Long amountData, BigDecimal price) {
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
