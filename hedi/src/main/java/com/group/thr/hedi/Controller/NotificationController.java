package com.group.thr.hedi.Controller;

import com.group.thr.hedi.DTO.Common.Response.ResponseFormat;
import com.group.thr.hedi.Enum.ResponseCode;
import com.group.thr.hedi.Service.Interface.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*", maxAge = 3600)
public class NotificationController {

    @Autowired
    private INotificationService notificationService;

    @GetMapping("/user/{userId}")
    public ResponseFormat getUserNotifications(@PathVariable Long userId) {
        return new ResponseFormat(ResponseCode.SUCCESS, notificationService.getUserNotifications(userId));
    }

    @PutMapping("/user/{userId}/read")
    public ResponseFormat markAllAsRead(@PathVariable Long userId) {
        notificationService.markAllAsRead(userId);
        return new ResponseFormat(ResponseCode.SUCCESS, "Marked as read", null);
    }
}