package com.group.thr.hedi.Service.Interface;

import com.group.thr.hedi.DTO.Threshold.Request.ThresholdBatchRequest;
import com.group.thr.hedi.DTO.Threshold.Request.ThresholdToggleRequest;
import com.group.thr.hedi.DTO.Threshold.Response.ThresholdResponse;

import java.util.List;

public interface IThresholdService {
    void saveBatchThresholds(Long userId, List<ThresholdBatchRequest> requests);
    List<ThresholdResponse> getUserThresholds(Long userId);
    void updateThresholdSettings(Long userId, List<ThresholdToggleRequest> requests);
}