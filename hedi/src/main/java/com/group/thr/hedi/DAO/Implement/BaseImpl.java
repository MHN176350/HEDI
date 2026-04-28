package com.group.thr.hedi.DAO.Implement;

import org.springframework.data.jpa.repository.JpaRepository;

import com.group.thr.hedi.DAO.Interface.Base;


public class BaseImpl<T, ID> implements Base<T, ID> {
    private final JpaRepository<T, ID> repository;
    protected BaseImpl(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }
    @Override
    public T save(T entity) {
        return repository.save(entity);
       
    }

    @Override
    public T findById(ID id) {
        
        return repository.findById(id).orElse(null); 
    }

    @Override
    public void deleteById(ID id) {
        repository.deleteById(id);
    }

    @Override
    public java.util.List<T> findAll() {
       return repository.findAll();
    }

}
