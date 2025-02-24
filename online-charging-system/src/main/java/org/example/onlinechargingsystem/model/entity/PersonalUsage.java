package org.example.onlinechargingsystem.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "T_PERSONAL_USAGE_ID")
public class PersonalUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "GIVER_ID")
    private Long giverId;

    @Column(name = "RECEIVER_ID")
    private Long receiverId;

    @Column(name = "USAGE_TYPE")
    private String usageType; // "MINUTES", "SMS", "DATA"

    @Column(name = "USAGE_DATE")
    private Date usageDate;

    @Column(name = "USAGE_DURATION")
    private Integer usageDuration;
}

