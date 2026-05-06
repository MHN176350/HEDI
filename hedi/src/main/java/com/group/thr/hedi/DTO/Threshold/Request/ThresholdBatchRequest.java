
package com.group.thr.hedi.DTO.Threshold.Request;

import lombok.Data;

@Data
public class ThresholdBatchRequest {
    private Long metricId;
    private double minValue;
    private double maxValue;
}