package com.ramobeko.ocsandroidapp.data.model;

import java.util.Date;
import java.util.List;

public class Subscriber {

    private Long id;
    private Customer customer;
    private PackagePlan packagePlan;
    private String phoneNumber;
    private Date startDate;
    private Date endDate;
    private String status;
    private List<Balance> balances;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public PackagePlan getPackagePlan() {
        return packagePlan;
    }

    public void setPackagePlan(PackagePlan packagePlan) {
        this.packagePlan = packagePlan;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Balance> getBalances() {
        return balances;
    }

    public void setBalances(List<Balance> balances) {
        this.balances = balances;
    }

    // Inner classes for Customer, PackagePlan, and Balance

    public static class PackagePlan {
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

    public static class Balance {
        private Long id;
        private int levelMinutes;
        private int levelSms;
        private int levelData;
        private Date startDate;
        private Date endDate;

        // Getters and setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public int getLevelMinutes() {
            return levelMinutes;
        }

        public void setLevelMinutes(int levelMinutes) {
            this.levelMinutes = levelMinutes;
        }

        public int getLevelSms() {
            return levelSms;
        }

        public void setLevelSms(int levelSms) {
            this.levelSms = levelSms;
        }

        public int getLevelData() {
            return levelData;
        }

        public void setLevelData(int levelData) {
            this.levelData = levelData;
        }

        public Date getStartDate() {
            return startDate;
        }

        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }

        public Date getEndDate() {
            return endDate;
        }

        public void setEndDate(Date endDate) {
            this.endDate = endDate;
        }
    }
}
