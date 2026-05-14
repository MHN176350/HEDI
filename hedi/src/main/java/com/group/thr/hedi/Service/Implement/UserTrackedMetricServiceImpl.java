package com.group.thr.hedi.Service.Implement;

import com.group.thr.hedi.DTO.TrackedMetric.Request.TrackedMetricRequest;
import com.group.thr.hedi.DTO.TrackedMetric.Request.TrackedMetricToggleRequest;
import com.group.thr.hedi.DTO.TrackedMetric.Response.TrackedMetricResponse;
import com.group.thr.hedi.Entity.Metric;
import com.group.thr.hedi.Entity.User;
import com.group.thr.hedi.Entity.UserTrackedMetric;
import com.group.thr.hedi.Repository.IAuthenticationRepository;
import com.group.thr.hedi.Repository.IHealthMetricRepository;
import com.group.thr.hedi.Repository.IUserTrackedMetricRepository;
import com.group.thr.hedi.Service.Interface.IUserTrackedMetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserTrackedMetricServiceImpl implements IUserTrackedMetricService {

    @Autowired
    private IUserTrackedMetricRepository trackedMetricRepository;

    @Autowired
    private IAuthenticationRepository userRepository;

    @Autowired
    private IHealthMetricRepository healthMetricRepository;

    @Override
    public void saveBatchMetrics(Long userId, List<TrackedMetricRequest> requests) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        for (TrackedMetricRequest req : requests) {
            Metric metric = healthMetricRepository.findById(req.getMetricId())
                    .orElseThrow(() -> new RuntimeException("Metric not found"));

            trackedMetricRepository.findByUserIdAndMetricId(userId, metric.getId())
                .ifPresentOrElse(
                    existing -> {
                        existing.setActive(true);
                        trackedMetricRepository.save(existing);
                    },
                    () -> {
                        UserTrackedMetric newTracked = UserTrackedMetric.builder()
                                .user(user)
                                .metric(metric)
                                .isActive(true)
                                .consecutiveWarnings(0)
                                .consecutiveAlerts(0)
                                .currentTrend("INITIALIZING")
                                .build();
                        trackedMetricRepository.save(newTracked);
                    }
                );
        }
    }

    @Override
    public List<TrackedMetricResponse> getUserTrackedMetrics(Long userId) {
        return trackedMetricRepository.findByUserId(userId).stream().filter(UserTrackedMetric::isActive)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void updateMetricSettings(Long userId, List<TrackedMetricToggleRequest> requests) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        for (TrackedMetricToggleRequest req : requests) {
            trackedMetricRepository.findByUserIdAndMetricId(userId, req.getMetricId())
                .ifPresentOrElse(
                    existing -> {
                        existing.setActive(req.isActive());
                        trackedMetricRepository.save(existing);
                    },
                    () -> {
                        if (req.isActive()) {
                            Metric metric = healthMetricRepository.findById(req.getMetricId())
                                .orElseThrow(() -> new RuntimeException("Metric not found"));
                            
                            UserTrackedMetric newTracked = UserTrackedMetric.builder()
                                    .user(user)
                                    .metric(metric)
                                    .isActive(true)
                                    .consecutiveWarnings(0)
                                    .consecutiveAlerts(0)
                                    .currentTrend("INITIALIZING")
                                    .build();
                            trackedMetricRepository.save(newTracked);
                        }
                    }
                );
        }
    }

    private TrackedMetricResponse mapToResponse(UserTrackedMetric entity) {
        TrackedMetricResponse res = new TrackedMetricResponse();
        res.setId(entity.getId());
        res.setMetricId(entity.getMetric().getId());
        res.setMetricName(entity.getMetric().getName());
        res.setUnit(entity.getMetric().getUnit());
        res.setActive(entity.isActive());
        res.setCurrentPersonalBaseline(entity.getCurrentPersonalBaseline());
        res.setConsecutiveWarnings(entity.getConsecutiveWarnings());
        res.setConsecutiveAlerts(entity.getConsecutiveAlerts());
        res.setCurrentTrend(entity.getCurrentTrend());
        return res;
    }
}