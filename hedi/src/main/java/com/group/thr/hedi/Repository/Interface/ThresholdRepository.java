package com.group.thr.hedi.Repository.Interface;

import com.group.thr.hedi.Entity.Threshold;
import com.group.thr.hedi.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ThresholdRepository extends JpaRepository<Threshold, Long> {
    List<Threshold> findByUserAndIsActive(User user, boolean isActive);
}