package com.group.thr.hedi.DTO.HealthRecord.Request;

import com.group.thr.hedi.Entity.HealthRecord;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HealthRecordRequest {

    @NotNull(message = "Metric type is required")
    private HealthRecord.MetricType metricType;

    @NotNull(message = "Metric value is required")
    @DecimalMin(value = "0.0", message = "Metric value must be non-negative")
    private Double metricValue;

    private LocalDateTime recordedAt;
}