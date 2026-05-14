package com.group.thr.hedi.Service.Implement;

import com.group.thr.hedi.DTO.HealthRecord.Request.HealthRecordRequest;
import com.group.thr.hedi.DTO.HealthRecord.Response.HealthRecordResponse;
import com.group.thr.hedi.Entity.HealthRecord;
import com.group.thr.hedi.Entity.Metric;
import com.group.thr.hedi.Entity.User;
import com.group.thr.hedi.Entity.UserTrackedMetric;
import com.group.thr.hedi.Repository.IAuthenticationRepository;
import com.group.thr.hedi.Repository.IHealthMetricRepository;
import com.group.thr.hedi.Repository.IHealthRecordRepository;
import com.group.thr.hedi.Repository.IUserTrackedMetricRepository;
import com.group.thr.hedi.Service.Interface.IHealthRecordService;
import com.group.thr.hedi.Service.Interface.INotificationService;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HealthRecordServiceImpl implements IHealthRecordService {

    @Autowired
    private IHealthRecordRepository healthRecordRepository;

    @Autowired
    private IAuthenticationRepository userRepository;

    @Autowired
    private IUserTrackedMetricRepository trackedMetricRepository;

    @Autowired
    private IHealthMetricRepository healthMetricRepository;

    @Autowired
    private INotificationService notificationService;

    private static final int BASELINE_THRESHOLD = 3;

    @Override
    public HealthRecordResponse createRecord(Long userId, HealthRecordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Metric metricDef = healthMetricRepository.findAll().stream()
                .filter(m -> m.getName().equals(request.getMetricType()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Metric definition not found"));

        List<HealthRecord> history = healthRecordRepository
                .findTop5ByUserIdAndMetricIdOrderByRecordedAtDesc(userId, metricDef.getId());

        HealthRecord record = HealthRecord.builder()
                .user(user)
                .metric(metricDef)
                .metricValue(request.getMetricValue())
                .recordedAt(request.getRecordedAt() != null ? request.getRecordedAt() : LocalDateTime.now())
                .build();
        HealthRecord savedRecord = healthRecordRepository.save(record);

        UserTrackedMetric trackingProfile = trackedMetricRepository.findByUserIdAndMetricId(userId, metricDef.getId())
                .orElse(null);

        evaluateAndStoreAnalytics(user, metricDef, savedRecord.getMetricValue(), history, trackingProfile);

        return mapToResponse(savedRecord);
    }

    private void evaluateAndStoreAnalytics(User user, Metric metric, double currentValue, List<HealthRecord> history, UserTrackedMetric profile) {
        String severity = "INFO";
        StringBuilder notificationMsg = new StringBuilder();
        String formattedMetric = metric.getName().replace("_", " ");

        boolean isAbsoluteHigh = currentValue > metric.getMaxLimit();
        boolean isAbsoluteLow = currentValue < metric.getMinLimit();
        
        double deviation = 0;
        if (isAbsoluteHigh) deviation = ((currentValue - metric.getMaxLimit()) / metric.getMaxLimit()) * 100;
        else if (isAbsoluteLow) deviation = metric.getMinLimit() == 0 ? 100 : ((metric.getMinLimit() - currentValue) / metric.getMinLimit()) * 100;

        if (isAbsoluteHigh || isAbsoluteLow) {
            severity = deviation > 10.0 ? "ALERT" : "WARNING";
            
            String dir = isAbsoluteHigh ? "above" : "below";
            double limit = isAbsoluteHigh ? metric.getMaxLimit() : metric.getMinLimit();
            
            notificationMsg.append(String.format("Your %s is %s the healthy limit of %.1f %s. ", 
                    formattedMetric, dir, limit, metric.getUnit()));
        }
        if (profile != null) {
            if (severity.equals("ALERT")) {
                profile.setConsecutiveAlerts(profile.getConsecutiveAlerts() + 1);
                profile.setConsecutiveWarnings(0);
                
                if (profile.getConsecutiveAlerts() >= 3) {
                    notificationMsg.append("PREDICTIVE ALERT: You have had critical readings for 3 consecutive logs. Please consult a doctor.");
                }
            } else if (severity.equals("WARNING")) {
                profile.setConsecutiveWarnings(profile.getConsecutiveWarnings() + 1);
                profile.setConsecutiveAlerts(0);
            } else {
                profile.setConsecutiveAlerts(0);
                profile.setConsecutiveWarnings(0);
            }

            if (history.size() >= BASELINE_THRESHOLD) {
                double sum = currentValue;
                for (int i = 0; i < 4 && i < history.size(); i++) { 
                    sum += history.get(i).getMetricValue();
                }
                double newBaseline = sum / history.size();
                
                if (profile.getCurrentPersonalBaseline() != null) {
                    double trendVariance = ((newBaseline - profile.getCurrentPersonalBaseline()) / profile.getCurrentPersonalBaseline()) * 100;
                    if (trendVariance > 5.0) profile.setCurrentTrend("RISING");
                    else if (trendVariance < -5.0) profile.setCurrentTrend("DECLINING");
                    else profile.setCurrentTrend("STABLE");
                }
                profile.setCurrentPersonalBaseline(newBaseline);
            }

            trackedMetricRepository.save(profile);
        }

        if (!severity.equals("INFO")) {
            notificationService.createNotification(user.getId(), severity, notificationMsg.toString().trim());
        }
    }

    @Override
    public List<HealthRecordResponse> getRecordsByUserId(Long userId) {
        return healthRecordRepository.findAll().stream()
                .filter(record -> record.getUser().getId().equals(userId))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteRecord(Long id) {
        healthRecordRepository.deleteById(id);
    }

    @Override
    public HealthRecordResponse getLatestRecord(Long userId, String metricType) {
        Metric metricDef = healthMetricRepository.findAll().stream()
                .filter(m -> m.getName().equals(metricType))
                .findFirst()
                .orElse(null);

        if (metricDef == null) return null;

        HealthRecord record = healthRecordRepository
                .findTopByUserIdAndMetricIdOrderByRecordedAtDesc(userId, metricDef.getId())
                .orElse(null);
        return record != null ? mapToResponse(record) : null;
    }

    private HealthRecordResponse mapToResponse(HealthRecord record) {
        HealthRecordResponse response = new HealthRecordResponse();
        response.setId(record.getId());
        response.setMetricType(record.getMetric().getName());
        response.setMetricValue(record.getMetricValue());
        response.setRecordedAt(record.getRecordedAt());
        return response;
    }

    @Override
    @Transactional
    public void deleteAllRecordsByMetric(Long userId, String metricType) {
       Metric metricDef = healthMetricRepository.findAll().stream()
                .filter(m -> m.getName().equals(metricType))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Metric not found"));
                
      
        healthRecordRepository.deleteByUserIdAndMetricId(userId, metricDef.getId());
        
        trackedMetricRepository.findByUserIdAndMetricId(userId, metricDef.getId())
            .ifPresent(profile -> {
                profile.setCurrentPersonalBaseline(null);
                profile.setConsecutiveAlerts(0);
                profile.setConsecutiveWarnings(0);
                profile.setCurrentTrend("INITIALIZING");
                trackedMetricRepository.save(profile);
            });
    }
}