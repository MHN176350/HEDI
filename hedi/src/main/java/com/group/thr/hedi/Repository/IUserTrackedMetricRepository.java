package com.group.thr.hedi.Repository;

import com.group.thr.hedi.Entity.UserTrackedMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface IUserTrackedMetricRepository extends JpaRepository<UserTrackedMetric, Long> {
    List<UserTrackedMetric> findByUserId(Long userId);
    Optional<UserTrackedMetric> findByUserIdAndMetricId(Long userId, Long metricId);
}