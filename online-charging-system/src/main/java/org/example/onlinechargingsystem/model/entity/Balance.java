package org.example.onlinechargingsystem.model.entity;

import lombok.*;
import org.apache.ignite.cache.query.annotations.QuerySqlField;
import java.io.Serializable;


@Data // Lombok ile tüm getter, setter, equals, hashCode ve toString otomatik oluşturulacak
@NoArgsConstructor
@AllArgsConstructor
public class Balance implements Serializable {

    private static final long serialVersionUID = 1L;

    @QuerySqlField(index = true)
    private Long id;

    @QuerySqlField
    private Long packageId;

    @QuerySqlField(index = true)
    private Long subscriberId;

    @QuerySqlField
    private Integer levelMinutes;

    @QuerySqlField
    private Integer levelSms;

    @QuerySqlField
    private Integer levelData;
}
