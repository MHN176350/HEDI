package com.group.thr.hedi.Service.Interface;

import com.group.thr.hedi.DTO.HealthRecord.Request.HealthRecordRequest;
import com.group.thr.hedi.DTO.HealthRecord.Response.HealthRecordResponse;

public interface IHealthRecordService {
    HealthRecordResponse addHealthRecord(String userEmail, HealthRecordRequest request);
}