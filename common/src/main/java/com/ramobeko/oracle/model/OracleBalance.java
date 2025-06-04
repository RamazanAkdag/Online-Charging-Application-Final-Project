package com.ramobeko.oracle.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
import jakarta.persistence.*;
@Entity
@Table(name = "t_balance", schema = "aom")
public class OracleBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bal_id", nullable = false)
    private Long id;

    @JsonIgnore//to eliminate infinite recursion on json
    @ManyToOne
    @JoinColumn(name = "bal_subsc_id", referencedColumnName = "subsc_id", nullable = false)
    private OracleSubscriber subscriber;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "bal_pkg_id", referencedColumnName = "pkg_id", nullable = false)
    private OraclePackage packagePlan;

    @Column(name = "bal_lvl_minutes", nullable = true)
    private Long levelMinutes;

    @Column(name = "bal_lvl_sms", nullable = true)
    private Long levelSms;

    @Column(name = "bal_lvl_data", nullable = true)
    private Long levelData;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sdate", nullable = true)
    private Date startDate = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "edate", nullable = true)
    private Date endDate;

    // Constructors
    public OracleBalance() {
    }

    public OracleBalance(OracleSubscriber subscriber, OraclePackage packagePlan, Long levelMinutes, Long levelSms, Long levelData, Date startDate, Date endDate) {
        this.subscriber = subscriber;
        this.packagePlan = packagePlan;
        this.levelMinutes = levelMinutes;
        this.levelSms = levelSms;
        this.levelData = levelData;
        this.startDate = startDate != null ? startDate : new Date();
        this.endDate = endDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OracleSubscriber getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(OracleSubscriber subscriber) {
        this.subscriber = subscriber;
    }

    public OraclePackage getPackagePlan() {
        return packagePlan;
    }

    public void setPackagePlan(OraclePackage packagePlan) {
        this.packagePlan = packagePlan;
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
}
