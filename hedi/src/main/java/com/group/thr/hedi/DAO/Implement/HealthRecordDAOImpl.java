package com.group.thr.hedi.DAO.Implement;

import com.group.thr.hedi.DAO.Interface.IHealthRecordDAO;
import com.group.thr.hedi.Entity.HealthRecord;
import com.group.thr.hedi.Repository.IHealthRecordRepository;
import org.springframework.stereotype.Repository;

@Repository
public class HealthRecordDAOImpl extends BaseImpl<HealthRecord, Long> implements IHealthRecordDAO {
    private final IHealthRecordRepository healthRecordRepository;
    
    protected HealthRecordDAOImpl(IHealthRecordRepository repository) {
        super(repository);
        this.healthRecordRepository = repository;
    }

    @Override
    public HealthRecord getLatestRecord(Long userId, String metricTypeStr) {
       try {
        HealthRecord.MetricType type = HealthRecord.MetricType.valueOf(metricTypeStr);
        return healthRecordRepository.findTopByUserIdAndMetricTypeOrderByRecordedAtDesc(userId, type)
                .orElse(null);
    } catch (IllegalArgumentException e) {
        throw new RuntimeException("Invalid metric type");
    }
    }
}
