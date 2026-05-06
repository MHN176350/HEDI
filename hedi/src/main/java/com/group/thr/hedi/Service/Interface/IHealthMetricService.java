package com.group.thr.hedi.Service.Interface;
import com.group.thr.hedi.Entity.Metric;
import java.util.List;

public interface IHealthMetricService {
    void initializeDefaultMetrics();
    List<Metric> getAllActiveMetrics();
}