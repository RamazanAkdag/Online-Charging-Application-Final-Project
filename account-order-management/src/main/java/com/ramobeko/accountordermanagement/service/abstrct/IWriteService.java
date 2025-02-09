package com.ramobeko.accountordermanagement.service.abstrct;


public interface IWriteService<T> {
    void create(T entity);
}