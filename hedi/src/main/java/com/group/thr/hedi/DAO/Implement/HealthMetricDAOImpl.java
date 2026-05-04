package com.group.thr.hedi.DAO.Implement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.group.thr.hedi.DAO.Interface.IHealthMetricDAO;
import com.group.thr.hedi.Entity.Metric;
import com.group.thr.hedi.Repository.IHealthMetricRepository;
@Repository
public class HealthMetricDAOImpl extends BaseImpl<Metric, Long> implements IHealthMetricDAO {
     private final IHealthMetricRepository healthMetricRepository;
 protected HealthMetricDAOImpl(IHealthMetricRepository repository) {
        super(repository);
        this.healthMetricRepository = repository;
 }


}
