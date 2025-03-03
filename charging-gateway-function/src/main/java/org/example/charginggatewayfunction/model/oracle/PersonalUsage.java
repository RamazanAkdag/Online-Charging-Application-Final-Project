package org.example.charginggatewayfunction.model.oracle;

import com.ramobeko.dgwtgf.model.UsageType;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "PERSONAL_USAGE_TB")
public class PersonalUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PERSONAL_USAGE_ID", nullable = false)
    private Long personalUsageId;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "GIVER_ID")
    private Long giverId;

    @Column(name = "USAGE_DATE")
    private LocalDate usageDate;

    // Enum değerini VARCHAR olarak saklamak için:
    @Enumerated(EnumType.STRING)
    @Column(name = "USAGE_TYPE")
    private UsageType usageType;

    // Tam zaman bilgisini saklamak için (TIMESTAMP(6) WITH TIME ZONE):
    @Column(name = "USAGE_TIMESTAMP")
    private OffsetDateTime usageTimestamp;

    @Column(name = "USAGE_DURATION")
    private Integer usageDuration;

    public PersonalUsage() {
    }

    // İsteğe bağlı tüm alanları içeren constructor
    public PersonalUsage(Long personalUsageId, Long userId, Long giverId,
                         LocalDate usageDate, UsageType usageType,
                         OffsetDateTime usageTimestamp, Integer usageDuration) {
        this.personalUsageId = personalUsageId;
        this.userId = userId;
        this.giverId = giverId;
        this.usageDate = usageDate;
        this.usageType = usageType;
        this.usageTimestamp = usageTimestamp;
        this.usageDuration = usageDuration;
    }

    public Long getPersonalUsageId() {
        return personalUsageId;
    }

    public void setPersonalUsageId(Long personalUsageId) {
        this.personalUsageId = personalUsageId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public OffsetDateTime getUsageTimestamp() {
        return usageTimestamp;
    }

    public void setUsageTimestamp(OffsetDateTime usageTimestamp) {
        this.usageTimestamp = usageTimestamp;
    }

    public Integer getUsageDuration() {
        return usageDuration;
    }

    public void setUsageDuration(Integer usageDuration) {
        this.usageDuration = usageDuration;
    }
}
