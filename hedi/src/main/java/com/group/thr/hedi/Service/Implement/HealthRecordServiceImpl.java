package com.group.thr.hedi.Service.Implement;

import com.group.thr.hedi.DAO.Interface.IHealthRecordDAO;
import com.group.thr.hedi.DTO.HealthRecord.Request.HealthRecordRequest;
import com.group.thr.hedi.DTO.HealthRecord.Response.HealthRecordResponse;
import com.group.thr.hedi.Entity.HealthRecord;
import com.group.thr.hedi.Entity.User;
import com.group.thr.hedi.Repository.IAuthenticationRepository;
import com.group.thr.hedi.Service.Interface.IHealthRecordService;
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

        return mapToResponse(healthRecordDAO.save(record));
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