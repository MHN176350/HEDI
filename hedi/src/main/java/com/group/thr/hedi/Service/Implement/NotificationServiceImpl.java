package com.group.thr.hedi.Service.Implement;

import com.group.thr.hedi.DTO.Notification.Response.NotificationResponse;
import com.group.thr.hedi.Entity.Notification;
import com.group.thr.hedi.Entity.User;
import com.group.thr.hedi.Enum.NotificationType;
import com.group.thr.hedi.Repository.IAuthenticationRepository;
import com.group.thr.hedi.Repository.INotificationRepository;
import com.group.thr.hedi.Service.Interface.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements INotificationService {

    @Autowired
    private INotificationRepository notificationRepository;
    @Autowired
    private IAuthenticationRepository userRepository;
    @Autowired
    private SseNotificationService sseService;

    @Override
    public void createNotification(Long userId, String typeStr, String message) {
        User user = userRepository.findById(userId).orElseThrow();
        NotificationType type = NotificationType.valueOf(typeStr);

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setType(type);
        notification.setMessage(message);
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(notification);
        sseService.dispatchAlert(userId);
    }

    @Override
    public List<NotificationResponse> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream().map(n -> {
            NotificationResponse res = new NotificationResponse();
            res.setId(n.getId());
            res.setType(n.getType());
            res.setMessage(n.getMessage());
            res.setRead(n.isRead());
            res.setCreatedAt(n.getCreatedAt());
            return res;
        }).collect(Collectors.toList());
    }

    @Override
    public void markAllAsRead(Long userId) {
        List<Notification> unread = notificationRepository.findByUserIdAndIsReadFalse(userId);
        unread.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(unread);
    }
}