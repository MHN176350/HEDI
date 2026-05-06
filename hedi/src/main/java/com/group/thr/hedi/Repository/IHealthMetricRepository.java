package com.group.thr.hedi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.group.thr.hedi.Entity.Metric;

public interface IHealthMetricRepository extends JpaRepository<Metric, Long> {

}
