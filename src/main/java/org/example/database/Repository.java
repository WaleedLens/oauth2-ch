package org.example.database;

import java.util.List;

public interface Repository<T> {
    long save(T object);
    long delete(T object);
    long update(T object);
    T find(Long id);
    List<T> findAll();
}
