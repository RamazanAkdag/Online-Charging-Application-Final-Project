package com.ramobeko.accountordermanagement.service.abstrct;

import java.util.List;
import java.util.Map;

public interface IReadService<T> {
    T readById(Long id);
    List<T> readAll();
    List<T> readByCriteria(Map<String, Object> criteria);
}
