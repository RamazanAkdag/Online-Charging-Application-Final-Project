package org.example.onlinechargingsystem.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "T_BALANCE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "BalanceCache")
public class Balance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @QuerySqlField(index = true) // Ignite için SQL indeksleme
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BAL_ID")
    private Long id;

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

    @QuerySqlField(index = true) // Ignite indexleme için gerekli
    @Column(name = "BAL_SUBSCRIBER_ID")
    private Long subscriberId; // ➜ **Eksik olan alan eklendi**
}
