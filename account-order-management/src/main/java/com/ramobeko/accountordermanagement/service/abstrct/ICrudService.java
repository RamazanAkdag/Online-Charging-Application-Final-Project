package com.ramobeko.accountordermanagement.service.abstrct;


import com.ramobeko.accountordermanagement.model.dto.IDTO;

import java.util.List;


import java.util.List;

public interface ICrudService<T, DTO extends IDTO> {
    List<T> readAll();

    default void create(DTO dto) {
        create(null, dto);
    }

    void create(Long id, DTO dto);
    T readById(Long id);

    void update(DTO dto);
    void delete(Long id);
}

