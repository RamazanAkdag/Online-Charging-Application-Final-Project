package org.example.onlinechargingsystem.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import org.apache.ignite.cache.query.annotations.QuerySqlField;
import java.io.Serializable;


@Entity
@Getter
@Setter
@Table(name = "T_BALANCE")
public class Balance implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @QuerySqlField(index = true) // Ignite için indeksleme ekledik
    @Column(name = "BAL_ID")
    private Long id;

    @QuerySqlField(index = true) // Ignite indeksleme için kullanıyor
    @Column(name = "BAL_SUBSC_ID", nullable = false)
    private Long balSubscId; // subscriberId yerine kullanılacak

    @QuerySqlField
    @Column(name = "BAL_PKG_ID")
    private Long packageId;

    @QuerySqlField
    @Column(name = "BAL_LVL_MINUTES")
    private Integer levelMinutes;

    @QuerySqlField
    @Column(name = "BAL_LVL_SMS")
    private Integer levelSms;

    @QuerySqlField
    @Column(name = "BAL_LVL_DATA")
    private Integer levelData;
}




