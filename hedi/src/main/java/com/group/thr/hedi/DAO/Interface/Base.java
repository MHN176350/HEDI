package com.group.thr.hedi.DAO.Interface;

import java.util.List;

public interface Base<T,ID> {
    T save(T entity);
    T findById(ID id);
    void deleteById(ID id);
    List<T> findAll();
}
