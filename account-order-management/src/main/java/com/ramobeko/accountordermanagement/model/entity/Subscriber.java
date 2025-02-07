package com.ramobeko.accountordermanagement.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "t_subscriber", schema = "AOM")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Subscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subsc_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "subsc_cust_id", nullable = false)
    private Customer customer;

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

    public enum SubscriptionStatus {
        ACTIVE,
        INACTIVE
    }

}

