package org.example.charginggatewayfunction.model.oracle;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "PERSONAL_USAGE_TB")
public class PersonalUsage {

    @Id

    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(name = "PERSONAL_USAGE_ID", nullable = false)
    private Long personalUsageId;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "GIVER_ID")
    private Long giverId;

    @Column(name = "USAGE_DATE")
    private LocalDate usageDate;

    // TIMESTAMP(6) WITH TIME ZONE için OffsetDateTime veya ZonedDateTime kullanılabilir.
    // Burada OffsetDateTime örnek olarak verilmiştir.
    @Column(name = "USAGE_TYPE")
    private OffsetDateTime usageType;

    @Column(name = "USAGE_DURATION")
    private Integer usageDuration;

    // Boş constructor
    public PersonalUsage() {
    }

    // Dolu constructor
    public PersonalUsage(Long personalUsageId, Long userId, Long giverId,
                         LocalDate usageDate, OffsetDateTime usageType, Integer usageDuration) {
        this.personalUsageId = personalUsageId;
        this.userId = userId;
        this.giverId = giverId;
        this.usageDate = usageDate;
        this.usageType = usageType;
        this.usageDuration = usageDuration;
    }

    // Getter ve Setter metotları
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

    public Long getBidderId() {
        return giverId;
    }

    public void setBidderId(Long bidderId) {
        this.giverId = bidderId;
    }

    public LocalDate getUsageDate() {
        return usageDate;
    }

    public void setUsageDate(LocalDate usageDate) {
        this.usageDate = usageDate;
    }

    public OffsetDateTime getUsageType() {
        return usageType;
    }

    public void setUsageType(OffsetDateTime usageType) {
        this.usageType = usageType;
    }

    public Integer getUsageDuration() {
        return usageDuration;
    }

    public void setUsageDuration(Integer usageDuration) {
        this.usageDuration = usageDuration;
    }
}

