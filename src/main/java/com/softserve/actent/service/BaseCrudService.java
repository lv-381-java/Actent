package com.softserve.actent.service;

import java.util.List;

public interface BaseCrudService<E> {
    E add(E entity);

    List<E> getAll();

    E get(Long id);

    void delete(Long id);
}