package com.ramobeko.accountordermanagement.model.entity.oracle;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "t_subscriber", schema = "AOM")
public class OracleSubscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subsc_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "subsc_cust_id", nullable = false)
    private OracleCustomer oracleCustomer;

    @Column(name = "phone_number", unique = true, nullable = false, length = 11)
    private String phoneNumber;

    @Column(name = "subsc_start_date")
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(name = "subsc_end_date")
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Column(name = "subsc_status")
    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OracleCustomer getCustomer() {
        return oracleCustomer;
    }

    public void setCustomer(OracleCustomer oracleCustomer) {
        this.oracleCustomer = oracleCustomer;
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

    public SubscriptionStatus getStatus() {
        return status;
    }

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }

    public enum SubscriptionStatus {
        ACTIVE,
        INACTIVE
    }

}

