package com.group.thr.hedi.Service.Interface;

import com.group.thr.hedi.DTO.HealthRecord.Request.HealthRecordRequest;
import com.group.thr.hedi.Entity.HealthRecord;
import java.util.List;

public interface IHealthRecordService {
    HealthRecord createRecord(Long userId, HealthRecordRequest request);
    List<HealthRecord> getRecordsByUserId(Long userId);
    void deleteRecord(Long id);
}