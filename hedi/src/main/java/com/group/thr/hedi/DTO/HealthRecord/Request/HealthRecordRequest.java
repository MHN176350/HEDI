package com.group.thr.hedi.DTO.HealthRecord.Request;


import lombok.Data;
import java.time.LocalDateTime;

import com.group.thr.hedi.Enum.MetricType;

@Data
public class HealthRecordRequest {
    private MetricType metricType;
    private Double metricValue;
    private LocalDateTime recordedAt;
}