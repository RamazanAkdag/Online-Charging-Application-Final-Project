package org.example.onlinechargingsystem.model.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "T_BALANCE")
public class UserBalance implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BAL_ID")
    private Long id;

    @Column(name = "BAL_SUBSC_ID", nullable = false)
    private Long subscriberId;

    @Column(name = "BAL_PKG_ID", nullable = false)
    private Long packageId;

    @Column(name = "BAL_LVL_MINUTES")
    private Long availableMinutes;

    @Column(name = "BAL_LVL_SMS")
    private Long availableSms;

    @Column(name = "BAL_LVL_DATA")
    private Long availableData;

    @Column(name = "SDATE")
    private String startDate;

    @Column(name = "EDATE")
    private String endDate;

    public UserBalance() {
    }

    public UserBalance(Long id, Long subscriberId, Long packageId, Long availableMinutes, Long availableSms, Long availableData, String startDate, String endDate) {
        this.id = id;
        this.subscriberId = subscriberId;
        this.packageId = packageId;
        this.availableMinutes = availableMinutes;
        this.availableSms = availableSms;
        this.availableData = availableData;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(Long subscriberId) {
        this.subscriberId = subscriberId;
    }

    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    public Long getAvailableMinutes() {
        return availableMinutes;
    }

    public void setAvailableMinutes(Long availableMinutes) {
        this.availableMinutes = availableMinutes;
    }

    public Long getAvailableSms() {
        return availableSms;
    }

    public void setAvailableSms(Long availableSms) {
        this.availableSms = availableSms;
    }

    public Long getAvailableData() {
        return availableData;
    }

    public void setAvailableData(Long availableData) {
        this.availableData = availableData;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "UserBalance{" +
                "id=" + id +
                ", subscriberId=" + subscriberId +
                ", packageId=" + packageId +
                ", availableMinutes=" + availableMinutes +
                ", availableSms=" + availableSms +
                ", availableData=" + availableData +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
    }
}

