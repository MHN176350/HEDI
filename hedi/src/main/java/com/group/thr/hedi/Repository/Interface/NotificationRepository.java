package com.group.thr.hedi.Repository.Interface;

import com.group.thr.hedi.Entity.Notification;
import com.group.thr.hedi.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserAndIsReadOrderByCreatedAtDesc(User user, boolean isRead);
}