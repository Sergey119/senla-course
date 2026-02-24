package com.example.task.dao;

import java.util.List;
import java.util.Optional;

public interface GenericDAO<T> {

    Optional<T> findById(Integer id);
    boolean existsById(Integer id);
    List<T> findAll();
    T save(T entity);
    T update(T entity);
    void delete(Integer id);

}