package com.group.thr.hedi.Service.Interface;
import com.group.thr.hedi.DTO.Notification.Response.NotificationResponse;
import java.util.List;

public interface INotificationService {
    void createNotification(Long userId, String type, String message);
    List<NotificationResponse> getUserNotifications(Long userId);
    void markAllAsRead(Long userId);
}