package com.group.thr.hedi.DTO.Threshold.Request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ThresholdToggleRequest {
    private Long metricId;
    @JsonProperty("isActive")
    private boolean isActive;
    private double minValue;
    private double maxValue;
}