package com.example.task.dao;

import java.io.Serializable;
import java.util.List;

public interface GenericDao<T, PK extends Serializable> {
    PK save(T entity);
    void update(T entity);
    void delete(PK id);
    T findById(PK id);
    List<T> findAll();
    boolean existsById(PK id);
}