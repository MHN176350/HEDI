package com.group.thr.hedi.Service.Implement;

import com.group.thr.hedi.DAO.Interface.IHealthRecordDAO;
import com.group.thr.hedi.DTO.HealthRecord.Request.HealthRecordRequest;
import com.group.thr.hedi.DTO.HealthRecord.Response.HealthRecordResponse;
import com.group.thr.hedi.Entity.HealthRecord;
import com.group.thr.hedi.Entity.User;
import com.group.thr.hedi.Repository.IAuthenticationRepository;
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
    private IHealthRecordDAO healthRecordDAO;

    @Autowired
    private IAuthenticationRepository userRepository;

    @Autowired
    private IThresholdRepository thresholdRepository;

    @Autowired
    private INotificationService notificationService;

    @Override
    public HealthRecordResponse createRecord(Long userId, HealthRecordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        HealthRecord record = HealthRecord.builder()
                .user(user)
                .metricType(request.getMetricType())
                .metricValue(request.getMetricValue())
                .recordedAt(request.getRecordedAt() != null ? request.getRecordedAt() : LocalDateTime.now())
                .build();

        HealthRecord savedRecord = healthRecordDAO.save(record);

        thresholdRepository.findByUserId(userId).stream()
                .filter(t -> t.isActive() && t.getMetric().getName().equals(request.getMetricType().name()))
                .findFirst()
                .ifPresent(threshold -> {
                    double val = request.getMetricValue();
                    String metricName = threshold.getMetric().getName().replace("_", " ");
                    
                    if (val > threshold.getMaxValue()) {
                        notificationService.createNotification(userId, "WARNING", 
                            "High " + metricName + " alert: " + val + " " + threshold.getMetric().getUnit() + " exceeds your maximum limit of " + threshold.getMaxValue() + ".");
                    } else if (val < threshold.getMinValue()) {
                        notificationService.createNotification(userId, "WARNING", 
                            "Low " + metricName + " alert: " + val + " " + threshold.getMetric().getUnit() + " is below your minimum limit of " + threshold.getMinValue() + ".");
                    }
                });

        return mapToResponse(savedRecord);
    }

    @Override
    public List<HealthRecordResponse> getRecordsByUserId(Long userId) {
        return healthRecordDAO.findAll().stream()
                .filter(record -> record.getUser().getId().equals(userId))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteRecord(Long id) {
        healthRecordDAO.deleteById(id);
    }

    @Override
    public HealthRecordResponse getLatestRecord(Long userId, String metricType) {
       return  mapToResponse(healthRecordDAO.getLatestRecord(userId, metricType));
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