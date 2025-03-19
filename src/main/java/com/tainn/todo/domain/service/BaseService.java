package com.tainn.todo.domain.service;

import org.springframework.data.domain.Page;

import java.util.List;

public interface BaseService<T, K> {
    T save(T t);

    T update(T t);

    T getById(K id);

    void delete(K id);

    List<T> getAll();

    Page<T> getAll(int page, int size, String sort, String direction);
}
