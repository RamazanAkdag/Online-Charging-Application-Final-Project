package org.example.onlinechargingsystem.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.apache.ignite.cache.query.annotations.QuerySqlField;
import java.io.Serializable;

@Entity
@Table(name = "t_balance")
@Data // Lombok ile tüm getter, setter, equals, hashCode ve toString otomatik oluşturulacak
@NoArgsConstructor
@AllArgsConstructor
public class Balance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @QuerySqlField(index = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bal_id")
    private Long id;

    @QuerySqlField
    @Column(name = "bal_pkg_id")
    private Long packageId;

    @QuerySqlField(index = true)
    @Column(name = "bal_subscriber_id", nullable = false)
    private Long subscriberId;

    @QuerySqlField
    @Column(name = "bal_lvl_minutes")
    private Integer levelMinutes;

    @QuerySqlField
    @Column(name = "bal_lvl_sms")
    private Integer levelSms;

    @QuerySqlField
    @Column(name = "bal_lvl_data")
    private Integer levelData;
}
