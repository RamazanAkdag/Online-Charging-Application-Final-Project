package com.ramobeko.accountordermanagement.service.abstrct;


import java.util.List;

public interface ICrudService<T> {
    List<T> readAll();
    void create(T entity);
    T readById(Long id);
    void update(T entity);
    void delete(Long id);
}