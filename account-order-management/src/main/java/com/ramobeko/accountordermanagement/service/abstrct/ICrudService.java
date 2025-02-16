package com.ramobeko.accountordermanagement.service.abstrct;


import com.ramobeko.accountordermanagement.model.dto.IDTO;

import java.util.List;


import java.util.List;

public interface ICrudService<T, DTO extends IDTO> {
    List<T> readAll(); // DTO yerine T kullanıldı
    void create(DTO dto);
    T readById(Long id); // DTO yerine T kullanıldı
    void update(DTO dto);
    void delete(Long id);
}
