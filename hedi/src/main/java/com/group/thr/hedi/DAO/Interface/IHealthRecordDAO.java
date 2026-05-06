package com.group.thr.hedi.DAO.Interface;

import com.group.thr.hedi.Entity.HealthRecord;

public interface IHealthRecordDAO extends IBase<HealthRecord,Long> {
HealthRecord getLatestRecord(Long userId, String metricTypeStr);
}
