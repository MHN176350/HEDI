package com.group.thr.hedi.DTO.HealthRecord.Response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class HealthRecordResponse {
    private Long id;
    private String metricType;
    private Double metricValue;
    private LocalDateTime recordedAt;
}