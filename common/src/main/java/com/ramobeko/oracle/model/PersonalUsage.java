package com.ramobeko.oracle.model;

import com.ramobeko.dgwtgf.model.UsageType;

import java.time.LocalDate;
import java.time.OffsetDateTime;


import jakarta.persistence.*;


@Entity
@Table(name = "T_PERSONAL_USAGE")
public class PersonalUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PERSONAL_USAGE_ID", nullable = false)
    private Long personalUsageId;

    @Column(name = "RECEIVER_ID")
    private Long receiverId;

    @Column(name = "GIVER_ID")
    private Long giverId;

    @Column(name = "USAGE_DATE")
    private LocalDate usageDate;

    // Enum değerini VARCHAR olarak saklamak için:
    @Enumerated(EnumType.STRING)
    @Column(name = "USAGE_TYPE")
    private UsageType usageType;

    @Column(name = "USAGE_DURATION")
    private Integer usageDuration;

    public PersonalUsage() {
    }

    // İsteğe bağlı tüm alanları içeren constructor
    public PersonalUsage(Long personalUsageId, Long userId, Long giverId,
                         LocalDate usageDate, UsageType usageType,
                         OffsetDateTime usageTimestamp, Integer usageDuration) {
        this.personalUsageId = personalUsageId;
        this.receiverId = userId;
        this.giverId = giverId;
        this.usageDate = usageDate;
        this.usageType = usageType;
        this.usageDuration = usageDuration;
    }

    public Long getPersonalUsageId() {
        return personalUsageId;
    }

    public void setPersonalUsageId(Long personalUsageId) {
        this.personalUsageId = personalUsageId;
    }

    public Long getUserId() {
        return receiverId;
    }

    public void setUserId(Long userId) {
        this.receiverId = userId;
    }

    public Long getGiverId() {
        return giverId;
    }

    public void setGiverId(Long giverId) {
        this.giverId = giverId;
    }

    public LocalDate getUsageDate() {
        return usageDate;
    }

    public void setUsageDate(LocalDate usageDate) {
        this.usageDate = usageDate;
    }

    public UsageType getUsageType() {
        return usageType;
    }

    public void setUsageType(UsageType usageType) {
        this.usageType = usageType;
    }

    public Integer getUsageDuration() {
        return usageDuration;
    }

    public void setUsageDuration(Integer usageDuration) {
        this.usageDuration = usageDuration;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }
}
