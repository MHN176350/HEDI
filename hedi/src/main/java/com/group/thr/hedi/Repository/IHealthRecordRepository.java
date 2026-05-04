package com.group.thr.hedi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.group.thr.hedi.Entity.HealthRecord;

public interface IHealthRecordRepository extends JpaRepository<HealthRecord, Long> {

}
