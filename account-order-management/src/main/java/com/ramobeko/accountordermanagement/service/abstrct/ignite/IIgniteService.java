package com.ramobeko.accountordermanagement.service.abstrct.ignite;

import com.ramobeko.accountordermanagement.service.abstrct.IWriteService;

public interface IIgniteService<T> extends IWriteService<T> {
    void update(T entity);
    void delete(Long id);
}