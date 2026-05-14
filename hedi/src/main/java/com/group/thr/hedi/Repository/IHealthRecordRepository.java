package com.group.thr.hedi.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.group.thr.hedi.Entity.HealthRecord;

public interface IHealthRecordRepository extends JpaRepository<HealthRecord, Long> {
    Optional<HealthRecord> findTopByUserIdAndMetricIdOrderByRecordedAtDesc(Long userId, Long metricId);
    List<HealthRecord> findTop5ByUserIdAndMetricIdOrderByRecordedAtDesc(Long userId, Long metricId);
     void deleteByUserIdAndMetricId(Long userId, Long metricId);
}