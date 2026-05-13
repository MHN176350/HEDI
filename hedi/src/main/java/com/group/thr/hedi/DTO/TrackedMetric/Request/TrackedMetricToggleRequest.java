package com.group.thr.hedi.DTO.TrackedMetric.Request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TrackedMetricToggleRequest {
    private Long metricId;
    @JsonProperty("isActive")
    private boolean isActive;
}