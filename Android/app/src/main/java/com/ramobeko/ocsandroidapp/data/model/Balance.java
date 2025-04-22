package com.ramobeko.ocsandroidapp.data.model;

import java.sql.Date;

public  class Balance {
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