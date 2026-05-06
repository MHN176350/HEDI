package com.group.thr.hedi.Service.Implement;

import com.group.thr.hedi.DTO.HealthRecord.Request.HealthRecordRequest;
import com.group.thr.hedi.DTO.HealthRecord.Response.HealthRecordResponse;
import com.group.thr.hedi.Entity.HealthRecord;
import com.group.thr.hedi.Entity.Notification;
import com.group.thr.hedi.Entity.Threshold;
import com.group.thr.hedi.Entity.User;
import com.group.thr.hedi.Enum.NotificationType;
import com.group.thr.hedi.Repository.Interface.AuthenticationRepository;
import com.group.thr.hedi.Repository.Interface.HealthRecordRepository;
import com.group.thr.hedi.Repository.Interface.NotificationRepository;
import com.group.thr.hedi.Repository.Interface.ThresholdRepository;
import com.group.thr.hedi.Service.Interface.IHealthRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class HealthRecordServiceImpl implements IHealthRecordService {

    private static final Map<HealthRecord.MetricType, double[]> ACCEPTABLE_RANGES = new EnumMap<>(HealthRecord.MetricType.class);

    static {
        ACCEPTABLE_RANGES.put(HealthRecord.MetricType.BLOOD_SUGAR, new double[]{0.0, 1000.0});
        ACCEPTABLE_RANGES.put(HealthRecord.MetricType.BLOOD_PRESSURE_SYSTOLIC, new double[]{0.0, 300.0});
        ACCEPTABLE_RANGES.put(HealthRecord.MetricType.BLOOD_PRESSURE_DIASTOLIC, new double[]{0.0, 200.0});
        ACCEPTABLE_RANGES.put(HealthRecord.MetricType.HEART_RATE, new double[]{0.0, 300.0});
        ACCEPTABLE_RANGES.put(HealthRecord.MetricType.SpO2_LEVEL, new double[]{0.0, 100.0});
    }

    @Autowired
    private AuthenticationRepository userRepository;

    @Autowired
    private HealthRecordRepository healthRecordRepository;

    @Autowired
    private ThresholdRepository thresholdRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    @Transactional
    public HealthRecordResponse addHealthRecord(String userEmail, HealthRecordRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        validateAcceptableRange(request.getMetricType(), request.getMetricValue());

        LocalDateTime recordedAt = request.getRecordedAt() != null ? request.getRecordedAt() : LocalDateTime.now();

        HealthRecord record = HealthRecord.builder()
                .user(user)
                .metricType(request.getMetricType())
                .metricValue(request.getMetricValue())
                .recordedAt(recordedAt)
                .build();

        HealthRecord saved = healthRecordRepository.save(record);

        String alertMessage = checkThresholdsAndNotify(user, request.getMetricType(), request.getMetricValue());

        return HealthRecordResponse.builder()
                .id(saved.getId())
                .metricType(saved.getMetricType())
                .metricValue(saved.getMetricValue())
                .recordedAt(saved.getRecordedAt())
                .alertTriggered(alertMessage != null)
                .alertMessage(alertMessage)
                .build();
    }

    private void validateAcceptableRange(HealthRecord.MetricType metricType, Double value) {
        double[] range = ACCEPTABLE_RANGES.get(metricType);
        if (range == null) return;
        if (value < range[0] || value > range[1]) {
            throw new IllegalArgumentException(
                    String.format("Value %.2f is out of acceptable range [%.0f, %.0f] for %s",
                            value, range[0], range[1], metricType.name())
            );
        }
    }

    private String checkThresholdsAndNotify(User user, HealthRecord.MetricType metricType, Double value) {
        List<Threshold> thresholds = thresholdRepository.findByUserAndIsActive(user, true);

        Optional<Threshold> matched = thresholds.stream()
                .filter(t -> t.getMetric() != null && t.getMetric().getName().equalsIgnoreCase(metricType.name()))
                .findFirst();

        if (matched.isEmpty()) return null;

        Threshold threshold = matched.get();
        String alertMessage = null;

        if (value > threshold.getMaxValue()) {
            alertMessage = String.format("Your %s reading of %.2f exceeds the maximum threshold of %.2f",
                    metricType.name(), value, threshold.getMaxValue());
        } else if (value < threshold.getMinValue()) {
            alertMessage = String.format("Your %s reading of %.2f is below the minimum threshold of %.2f",
                    metricType.name(), value, threshold.getMinValue());
        }

        if (alertMessage != null) {
            Notification notification = new Notification();
            notification.setType(NotificationType.WARNING);
            notification.setMessage(alertMessage);
            notification.setRead(false);
            notification.setCreatedAt(LocalDateTime.now());
            notification.setUser(user);
            notificationRepository.save(notification);
        }

        return alertMessage;
    }
}