package com.group.thr.hedi.Service.Implement;

import com.group.thr.hedi.DTO.HealthRecord.Response.HealthRecordResponse;
import com.group.thr.hedi.Entity.UserTrackedMetric;
import com.group.thr.hedi.Repository.IUserTrackedMetricRepository;
import com.group.thr.hedi.Service.Interface.IHealthRecordService;
import com.group.thr.hedi.Service.Interface.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DailyReminderScheduler {

    @Autowired
    private IUserTrackedMetricRepository trackedMetricRepository;
    @Autowired
    private IHealthRecordService healthRecordService;
    @Autowired
    private INotificationService notificationService;

    @Scheduled(cron = "0 0 10 * * ?")
    public void checkDailyLogs() {
        LocalDate today = LocalDate.now();

        List<UserTrackedMetric> activeTrackers = trackedMetricRepository.findAll().stream()
                .filter(UserTrackedMetric::isActive)
                .toList();

        for (UserTrackedMetric tracker : activeTrackers) {
            Long userId = tracker.getUser().getId();
            String metricName = tracker.getMetric().getName();

            HealthRecordResponse latestRecord = healthRecordService.getLatestRecord(userId, metricName);

            boolean loggedToday = latestRecord != null && latestRecord.getRecordedAt().toLocalDate().equals(today);
            
            if (!loggedToday) {
                String formattedName = metricName.replace("_", " ").toLowerCase();
                String message = "Friendly reminder: You haven't logged your " + formattedName + " today!";
                
                notificationService.createNotification(userId, "INFO", message);
            }
        }
    }
}