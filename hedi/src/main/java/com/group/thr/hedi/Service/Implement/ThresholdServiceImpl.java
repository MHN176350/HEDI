package com.group.thr.hedi.Service.Implement;

import com.group.thr.hedi.DTO.Threshold.Request.ThresholdBatchRequest;
import com.group.thr.hedi.Entity.Metric;
import com.group.thr.hedi.Entity.Threshold;
import com.group.thr.hedi.Entity.User;
import com.group.thr.hedi.Repository.IAuthenticationRepository;
import com.group.thr.hedi.Repository.IHealthMetricRepository;
import com.group.thr.hedi.Repository.IThresholdRepository;
import com.group.thr.hedi.Service.Interface.IThresholdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ThresholdServiceImpl implements IThresholdService {

    @Autowired
    private IThresholdRepository thresholdRepository;
    @Autowired
    private IAuthenticationRepository userRepository;
    @Autowired
    private IHealthMetricRepository metricRepository;

    @Override
    public void saveBatchThresholds(Long userId, List<ThresholdBatchRequest> requests) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Threshold> thresholds = requests.stream().map(req -> {
            Metric metric = metricRepository.findById(req.getMetricId())
                    .orElseThrow(() -> new RuntimeException("Metric not found"));
            
            Threshold threshold = new Threshold();
            threshold.setUser(user);
            threshold.setMetric(metric);
            threshold.setMinValue(req.getMinValue());
            threshold.setMaxValue(req.getMaxValue());
            threshold.setActive(true);
            return threshold;
        }).collect(Collectors.toList());

        thresholdRepository.saveAll(thresholds);
    }
}