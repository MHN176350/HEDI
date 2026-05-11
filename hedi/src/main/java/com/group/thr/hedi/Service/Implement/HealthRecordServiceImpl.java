package com.group.thr.hedi.Service.Implement;

import com.group.thr.hedi.DTO.HealthRecord.Request.HealthRecordRequest;
import com.group.thr.hedi.DTO.HealthRecord.Response.HealthRecordResponse;
import com.group.thr.hedi.Entity.HealthRecord;
import com.group.thr.hedi.Entity.Metric;
import com.group.thr.hedi.Entity.User;
import com.group.thr.hedi.Enum.MetricType;
import com.group.thr.hedi.Repository.IAuthenticationRepository;
import com.group.thr.hedi.Repository.IHealthMetricRepository;
import com.group.thr.hedi.Repository.IHealthRecordRepository;
import com.group.thr.hedi.Repository.IThresholdRepository;
import com.group.thr.hedi.Service.Interface.IHealthRecordService;
import com.group.thr.hedi.Service.Interface.INotificationService;
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
    private IThresholdRepository thresholdRepository;

    @Autowired
    private IHealthMetricRepository healthMetricRepository;

    @Autowired
    private INotificationService notificationService;

    private static final int BASELINE_THRESHOLD = 5; 

    @Override
    public HealthRecordResponse createRecord(Long userId, HealthRecordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Metric metricDef = healthMetricRepository.findAll().stream()
                .filter(m -> m.getName().equals(request.getMetricType().name()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Metric definition not found in database"));

        List<HealthRecord> history = healthRecordRepository
                .findTop5ByUserIdAndMetricTypeOrderByRecordedAtDesc(userId, request.getMetricType());

        HealthRecord record = HealthRecord.builder()
                .user(user)
                .metricType(request.getMetricType())
                .metricValue(request.getMetricValue())
                .recordedAt(request.getRecordedAt() != null ? request.getRecordedAt() : LocalDateTime.now())
                .build();
        HealthRecord savedRecord = healthRecordRepository.save(record);

        boolean isTracking = thresholdRepository.findByUserId(userId).stream()
                .anyMatch(t -> t.isActive() && t.getMetric().getName().equals(metricDef.getName()));

        if (isTracking || metricDef.getName().equals("BMI")) {
            evaluateDualCheckRule(user, metricDef, request.getMetricValue(), history);
        }

        return mapToResponse(savedRecord);
    }

    private void evaluateDualCheckRule(User user, Metric metric, double currentValue, List<HealthRecord> history) {
        String formattedMetric = metric.getName().replace("_", " ");
        StringBuilder notificationMsg = new StringBuilder();
        String severity = "INFO"; 
        boolean isAbsoluteHigh = currentValue > metric.getMaxLimit();
        boolean isAbsoluteLow = currentValue < metric.getMinLimit();

        if (isAbsoluteHigh) {
            notificationMsg.append(String.format("Medical Alert: Your %s (%.1f %s) is above the healthy limit (%.1f). ", 
                    formattedMetric, currentValue, metric.getUnit(), metric.getMaxLimit()));
            severity = "ALERT";
        } else if (isAbsoluteLow) {
            notificationMsg.append(String.format("Medical Alert: Your %s (%.1f %s) is below the healthy limit (%.1f). ", 
                    formattedMetric, currentValue, metric.getUnit(), metric.getMinLimit()));
            severity = "ALERT";
        }

   
        if (history.size() >= BASELINE_THRESHOLD) {
            double sum = 0;
            for (HealthRecord r : history) sum += r.getMetricValue();
            double average = sum / history.size();
            
            double deviation = ((currentValue - average) / average) * 100;
            double absDeviation = Math.abs(deviation);
            
            if (absDeviation >= 20.0) {
                String dir = deviation > 0 ? "spike" : "drop";
                notificationMsg.append(String.format("Significant %.1f%% %s detected compared to your personal baseline of %.1f! ", absDeviation, dir, average));
                severity = "ALERT"; 
            } else if (absDeviation >= 10.0) {
                String dir = deviation > 0 ? "increase" : "decrease";
                notificationMsg.append(String.format("Notice: %.1f%% %s compared to your normal average of %.1f. ", absDeviation, dir, average));
                if (severity.equals("INFO")) severity = "WARNING"; 
            }
        } else {
            if (!severity.equals("INFO")) {
               notificationMsg.append("(Baseline establishing... Evaluated using general standards).");
            }
        }
        if (!severity.equals("INFO") && notificationMsg.length() > 0) {
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
        try {
            MetricType type = MetricType.valueOf(metricType);
            HealthRecord record = healthRecordRepository.findTopByUserIdAndMetricTypeOrderByRecordedAtDesc(userId, type)
                    .orElse(null);
            return record != null ? mapToResponse(record) : null;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid metric type");
        }
    }

    private HealthRecordResponse mapToResponse(HealthRecord record) {
        HealthRecordResponse response = new HealthRecordResponse();
        response.setId(record.getId());
        response.setMetricType(record.getMetricType().name());
        response.setMetricValue(record.getMetricValue());
        response.setRecordedAt(record.getRecordedAt());
        return response;
    }
}