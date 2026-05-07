package com.group.thr.hedi.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.group.thr.hedi.Entity.Threshold;

public interface IThresholdRepository extends JpaRepository<Threshold, Long> {
    List<Threshold> findByUserId(Long userId);
    Optional<Threshold> findByUserIdAndMetricId(Long userId, Long metricId);
}