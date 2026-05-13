package com.group.thr.hedi.DTO.HealthRecord.Request;


import lombok.Data;
import java.time.LocalDateTime;

@Data
public class HealthRecordRequest {
    private String metricType;
    private Double metricValue;
    private LocalDateTime recordedAt;
}