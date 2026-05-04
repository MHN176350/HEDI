package com.group.thr.hedi.DTO.HealthRecord.Request;

import com.group.thr.hedi.Entity.HealthRecord.MetricType;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class HealthRecordRequest {
    private MetricType metricType;
    private Double metricValue;
    private LocalDateTime recordedAt;
}