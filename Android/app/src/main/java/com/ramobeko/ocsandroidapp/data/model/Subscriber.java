package com.ramobeko.ocsandroidapp.data.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Subscriber implements Serializable {

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

    @Override
    public String toString() {
        return "Subscriber{" +
                "id=" + id +
                ", customer=" + customer +
                ", packagePlan=" + packagePlan +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status='" + status + '\'' +
                ", balances=" + balances +
                '}';
    }
}
