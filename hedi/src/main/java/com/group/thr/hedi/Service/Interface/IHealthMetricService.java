package com.group.thr.hedi.Service.Interface;
import com.group.thr.hedi.DTO.Metric.Request.MetricRequest;
import com.group.thr.hedi.Entity.Metric;
import java.util.List;

public interface IHealthMetricService {
    void initializeDefaultMetrics();
    List<Metric> getAllActiveMetrics();
    List<Metric> getAllMetrics();
    Metric addMetric(MetricRequest metric);
    Metric toggleMetricStatus(Long id);
    Metric updateMetric(Long id, MetricRequest request);
}