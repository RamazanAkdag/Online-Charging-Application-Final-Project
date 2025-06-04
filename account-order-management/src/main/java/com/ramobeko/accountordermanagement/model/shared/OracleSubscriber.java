package com.ramobeko.accountordermanagement.model.shared;

import com.ramobeko.accountordermanagement.model.dto.IDTO;
import com.ramobeko.accountordermanagement.model.entity.oracle.OracleBalance;
import com.ramobeko.accountordermanagement.model.entity.oracle.OracleCustomer;
import com.ramobeko.accountordermanagement.model.entity.oracle.OraclePackage;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "t_subscriber", schema = "aom")
public class OracleSubscriber implements IDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subsc_id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "subsc_cust_id", referencedColumnName = "cust_id", nullable = false)
    private OracleCustomer customer;

    @ManyToOne
    @JoinColumn(name = "subsc_pkg_id", referencedColumnName = "pkg_id", nullable = true)
    private OraclePackage packagePlan;

    @Column(name = "phone_number", length = 11, nullable = false)
    private String phoneNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "subsc_start_date", nullable = true)
    private Date startDate = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "subsc_end_date", nullable = true)
    private Date endDate;

    @Column(name = "subsc_status", length = 20, nullable = true)
    private String status;

    @OneToMany(mappedBy = "subscriber", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OracleBalance> balances;

    // Constructors
    public OracleSubscriber() {
    }

    public OracleSubscriber(OracleCustomer customer, OraclePackage packagePlan, String phoneNumber, Date startDate, Date endDate, String status) {
        this.customer = customer;
        this.packagePlan = packagePlan;
        this.phoneNumber = phoneNumber;
        this.startDate = startDate != null ? startDate : new Date();
        this.endDate = endDate;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OracleCustomer getCustomer() {
        return customer;
    }

    public void setCustomer(OracleCustomer customer) {
        this.customer = customer;
    }

    public OraclePackage getPackagePlan() {
        return packagePlan;
    }

    public void setPackagePlan(OraclePackage packagePlan) {
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

    public List<OracleBalance> getBalances() {
        return balances;
    }

    public void setBalances(List<OracleBalance> balances) {
        this.balances = balances;
    }
}
