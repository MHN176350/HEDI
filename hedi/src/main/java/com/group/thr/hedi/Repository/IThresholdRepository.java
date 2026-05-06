package com.group.thr.hedi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.group.thr.hedi.Entity.Threshold;

public interface IThresholdRepository extends JpaRepository<Threshold, Long> {
}