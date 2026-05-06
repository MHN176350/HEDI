package com.group.thr.hedi.DTO.Threshold.Response;

import lombok.Data;

@Data
public class ThresholdResponse {
    private Long id;
    private Long metricId;
    private String metricName;
    private String unit;
    private double minValue;
    private double maxValue;
}