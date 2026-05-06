package com.group.thr.hedi.Service.Interface;

import com.group.thr.hedi.DTO.Threshold.Request.ThresholdBatchRequest;
import java.util.List;

public interface IThresholdService {
    void saveBatchThresholds(Long userId, List<ThresholdBatchRequest> requests);
}