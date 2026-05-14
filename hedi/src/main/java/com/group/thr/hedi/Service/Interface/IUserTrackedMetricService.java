package com.group.thr.hedi.Service.Interface;

import com.group.thr.hedi.DTO.TrackedMetric.Request.TrackedMetricRequest;
import com.group.thr.hedi.DTO.TrackedMetric.Request.TrackedMetricToggleRequest;
import com.group.thr.hedi.DTO.TrackedMetric.Response.TrackedMetricResponse;
import java.util.List;

public interface IUserTrackedMetricService {
    void saveBatchMetrics(Long userId, List<TrackedMetricRequest> requests);
    List<TrackedMetricResponse> getUserTrackedMetrics(Long userId);
    void updateMetricSettings(Long userId, List<TrackedMetricToggleRequest> requests);
}