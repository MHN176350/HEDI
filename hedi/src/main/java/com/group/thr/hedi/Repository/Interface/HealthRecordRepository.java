package com.group.thr.hedi.Repository.Interface;

import com.group.thr.hedi.Entity.HealthRecord;
import com.group.thr.hedi.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HealthRecordRepository extends JpaRepository<HealthRecord, Long> {
    List<HealthRecord> findByUserOrderByRecordedAtDesc(User user);
}