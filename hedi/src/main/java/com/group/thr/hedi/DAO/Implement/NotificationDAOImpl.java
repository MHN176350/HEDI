package com.group.thr.hedi.DAO.Implement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.group.thr.hedi.DAO.Interface.INotificationDAO;
import com.group.thr.hedi.Entity.Notification;
import com.group.thr.hedi.Repository.INotificationRepository;

@Repository
public class NotificationDAOImpl extends BaseImpl<Notification, Long> implements INotificationDAO  {
    private INotificationRepository notificationRepository;
    protected NotificationDAOImpl(JpaRepository<Notification, Long> repository, INotificationRepository notificationRepository) {
        super(repository);
        this.notificationRepository=notificationRepository;
    }

}
