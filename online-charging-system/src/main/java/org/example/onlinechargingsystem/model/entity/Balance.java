package org.example.onlinechargingsystem.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "T_BALANCE")
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BAL_ID")
    private Long id;

    @Column(name = "BAL_PKG_ID")
    private Long packageId;

    @Column(name = "BAL_LVL_MINUTES")
    private Integer levelMinutes;

    @Column(name = "BAL_LVL_SMS")
    private Integer levelSms;

    @Column(name = "BAL_LVL_DATA")
    private Integer levelData;
}

