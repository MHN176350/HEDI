package com.group.thr.hedi.Service.Interface;

import com.group.thr.hedi.DTO.HealthRecord.Request.HealthRecordRequest;
import com.group.thr.hedi.DTO.HealthRecord.Response.HealthRecordResponse;
import java.util.List;

public interface IHealthRecordService {
    HealthRecordResponse createRecord(Long userId, HealthRecordRequest request);
    List<HealthRecordResponse> getRecordsByUserId(Long userId);
    void deleteRecord(Long id);
    HealthRecordResponse getLatestRecord(Long userId, String metricType);
}