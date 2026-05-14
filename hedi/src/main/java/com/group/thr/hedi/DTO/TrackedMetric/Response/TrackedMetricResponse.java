package com.group.thr.hedi.DTO.TrackedMetric.Response;
import lombok.Data;

@Data
public class TrackedMetricResponse {
    private Long id;
    private Long metricId;
    private String metricName;
    private String unit;
    private boolean isActive;
    private Double currentPersonalBaseline;
    private int consecutiveWarnings;
    private int consecutiveAlerts;
    private String currentTrend;
}