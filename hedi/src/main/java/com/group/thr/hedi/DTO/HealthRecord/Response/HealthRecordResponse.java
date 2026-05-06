package com.group.thr.hedi.DTO.HealthRecord.Response;

import com.group.thr.hedi.Entity.HealthRecord;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class HealthRecordResponse {
    private Long id;
    private HealthRecord.MetricType metricType;
    private Double metricValue;
    private LocalDateTime recordedAt;
    private boolean alertTriggered;
    private String alertMessage;
}