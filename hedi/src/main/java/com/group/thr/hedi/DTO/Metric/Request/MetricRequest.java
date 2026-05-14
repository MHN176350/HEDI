package com.group.thr.hedi.DTO.Metric.Request;

import lombok.Data;

@Data
public class MetricRequest {
    private String name;
    private String unit;
    private String description;
    private String imgUrl;
    private Double minLimit;
    private Double maxLimit;
    private String themeColor;
}