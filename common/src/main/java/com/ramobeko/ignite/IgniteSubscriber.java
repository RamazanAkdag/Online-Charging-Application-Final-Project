package com.ramobeko.ignite;

import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.binary.BinaryReader;
import org.apache.ignite.binary.BinaryWriter;
import org.apache.ignite.binary.Binarylizable;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class IgniteSubscriber implements Binarylizable, Serializable {

    @QuerySqlField(index = true)
    private Long id;

    @QuerySqlField(index = true)
    private Long customerId;

    @QuerySqlField(index = true)
    private Long packagePlanId;

    @QuerySqlField(index = true)
    private String phoneNumber;

    @QuerySqlField(index = true)
    private Date startDate;

    @QuerySqlField(index = true)
    private Date endDate;

    @QuerySqlField(index = true)
    private String status;

    // ✅ Store full IgniteBalance objects instead of just IDs
    private List<IgniteBalance> balances;

    // Constructors
    public IgniteSubscriber() {
    }

    public IgniteSubscriber(Long id, Long customerId, Long packagePlanId, String phoneNumber, Date startDate, Date endDate, String status, List<IgniteBalance> balances) {
        this.id = id;
        this.customerId = customerId;
        this.packagePlanId = packagePlanId;
        this.phoneNumber = phoneNumber;
        this.startDate = startDate != null ? startDate : new Date();
        this.endDate = endDate;
        this.status = status;
        this.balances = balances;
    }

    // Binarylizable interface implementation
    @Override
    public void writeBinary(BinaryWriter writer) {
        writer.writeLong("id", id);
        writer.writeLong("customerId", customerId);
        writer.writeLong("packagePlanId", packagePlanId);
        writer.writeString("phoneNumber", phoneNumber);
        writer.writeDate("startDate", startDate);
        writer.writeDate("endDate", endDate);
        writer.writeString("status", status);
        writer.writeObject("balances", balances);
    }

    @Override
    public void readBinary(BinaryReader reader) {
        this.id = reader.readLong("id");
        this.customerId = reader.readLong("customerId");
        this.packagePlanId = reader.readLong("packagePlanId");
        this.phoneNumber = reader.readString("phoneNumber");
        this.startDate = reader.readDate("startDate");
        this.endDate = reader.readDate("endDate");
        this.status = reader.readString("status");
        this.balances = reader.readObject("balances");
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getPackagePlanId() {
        return packagePlanId;
    }

    public void setPackagePlanId(Long packagePlanId) {
        this.packagePlanId = packagePlanId;
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

    public List<IgniteBalance> getBalances() {
        return balances;
    }

    public void setBalances(List<IgniteBalance> balances) {
        this.balances = balances;
    }

    @Override
    public String toString() {
        return "IgniteSubscriber{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", packagePlanId=" + packagePlanId +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status='" + status + '\'' +
                ", balances=" + balances +
                '}';
    }
}
