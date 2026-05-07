package com.group.thr.hedi.DTO.Notification.Response;

import com.group.thr.hedi.Enum.NotificationType;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationResponse {
    private Long id;
    private NotificationType type;
    private String message;
    private boolean isRead;
    private LocalDateTime createdAt;
}