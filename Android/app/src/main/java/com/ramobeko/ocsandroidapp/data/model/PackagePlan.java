package com.ramobeko.ocsandroidapp.data.model;

public class PackagePlan {
    private Long id;
    private String name;
    private int amountMinutes;
    private int amountSms;
    private int amountData;
    private double price;

    // Getters and setters
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

    public int getAmountMinutes() {
        return amountMinutes;
    }

    public void setAmountMinutes(int amountMinutes) {
        this.amountMinutes = amountMinutes;
    }

    public int getAmountSms() {
        return amountSms;
    }

    public void setAmountSms(int amountSms) {
        this.amountSms = amountSms;
    }

    public int getAmountData() {
        return amountData;
    }

    public void setAmountData(int amountData) {
        this.amountData = amountData;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}