package com.ramobeko.ignite;

import org.apache.ignite.binary.Binarylizable;
import org.apache.ignite.binary.BinaryReader;
import org.apache.ignite.binary.BinaryWriter;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.io.Serializable;
import java.util.Date;

public class IgniteBalance implements Binarylizable, Serializable {

    @QuerySqlField(index = true)
    private Long id;

    @QuerySqlField(index = true)
    private Long subscriberId;

    @QuerySqlField(index = true)
    private Long packageId;

    @QuerySqlField
    private Long levelMinutes;

    @QuerySqlField
    private Long levelSms;

    @QuerySqlField
    private Long levelData;

    @QuerySqlField
    private Date startDate;

    @QuerySqlField
    private Date endDate;

    // Constructors
    public IgniteBalance() {
    }

    public IgniteBalance(Long id, Long subscriberId, Long packageId, Long levelMinutes, Long levelSms, Long levelData, Date startDate, Date endDate) {
        this.id = id;
        this.subscriberId = subscriberId;
        this.packageId = packageId;
        this.levelMinutes = levelMinutes;
        this.levelSms = levelSms;
        this.levelData = levelData;
        this.startDate = startDate != null ? startDate : new Date();
        this.endDate = endDate;
    }

    // ✅ Binarylizable implementation (Apache Ignite Serialization)
    @Override
    public void writeBinary(BinaryWriter writer) {
        writer.writeLong("id", id);
        writer.writeLong("subscriberId", subscriberId);
        writer.writeLong("packageId", packageId);
        writer.writeLong("levelMinutes", levelMinutes);
        writer.writeLong("levelSms", levelSms);
        writer.writeLong("levelData", levelData);
        writer.writeDate("startDate", startDate);
        writer.writeDate("endDate", endDate);
    }

    @Override
    public void readBinary(BinaryReader reader) {
        this.id = reader.readLong("id");
        this.subscriberId = reader.readLong("subscriberId");
        this.packageId = reader.readLong("packageId");
        this.levelMinutes = reader.readLong("levelMinutes");
        this.levelSms = reader.readLong("levelSms");
        this.levelData = reader.readLong("levelData");
        this.startDate = reader.readDate("startDate");
        this.endDate = reader.readDate("endDate");
    }

    // Getters and Setters
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

    public Long getLevelMinutes() {
        return levelMinutes;
    }

    public void setLevelMinutes(Long levelMinutes) {
        this.levelMinutes = levelMinutes;
    }

    public Long getLevelSms() {
        return levelSms;
    }

    public void setLevelSms(Long levelSms) {
        this.levelSms = levelSms;
    }

    public Long getLevelData() {
        return levelData;
    }

    public void setLevelData(Long levelData) {
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

    @Override
    public String toString() {
        return "IgniteBalance{" +
                "id=" + id +
                ", subscriberId=" + subscriberId +
                ", packageId=" + packageId +
                ", levelMinutes=" + levelMinutes +
                ", levelSms=" + levelSms +
                ", levelData=" + levelData +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
