package com.group.thr.hedi.Service.Implement;

import com.group.thr.hedi.DTO.Metric.Request.MetricRequest;
import com.group.thr.hedi.Entity.Metric;
import com.group.thr.hedi.Enum.MetricType;
import com.group.thr.hedi.Repository.IHealthMetricRepository;
import com.group.thr.hedi.Service.Interface.IHealthMetricService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HealthMetricServiceImpl implements IHealthMetricService {

    @Autowired
    private IHealthMetricRepository healthMetricRepository;

    @Override
    @Transactional
    public void initializeDefaultMetrics() {
        for (MetricType type : MetricType.values()) {

            boolean exists = healthMetricRepository.findAll().stream()
                    .anyMatch(metric -> metric.getName().equals(type.name()));

            if (!exists) {
                Metric newMetric = new Metric();
                newMetric.setName(type.name());
                newMetric.setActive(true);
                
                switch (type) {
                    case BLOOD_SUGAR:
                        newMetric.setUnit("mg/dL");
                        newMetric.setDescription("Measures the amount of glucose in the blood.");
                        newMetric.setImgUrl("https://images.unsplash.com/photo-1579684385127-1ef15d508118?w=500&auto=format&fit=crop&q=60");
                        newMetric.setMinLimit(70.0);
                        newMetric.setMaxLimit(100.0);
                        newMetric.setThemeColor("#3b82f6"); 
                        break;
                    case BLOOD_PRESSURE_SYSTOLIC:
                        newMetric.setUnit("mmHg");
                        newMetric.setDescription("Pressure in arteries when the heart beats.");
                        newMetric.setImgUrl("https://images.unsplash.com/photo-1631549916768-4119b2e5f926?w=500&auto=format&fit=crop&q=60");
                        newMetric.setMinLimit(90.0);
                        newMetric.setMaxLimit(120.0);
                        newMetric.setThemeColor("#a855f7"); 
                        break;
                    case BLOOD_PRESSURE_DIASTOLIC:
                        newMetric.setUnit("mmHg");
                        newMetric.setDescription("Pressure in arteries between heartbeats.");
                        newMetric.setImgUrl("https://images.unsplash.com/photo-1584017911766-d451b3d0e843?w=500&auto=format&fit=crop&q=60");
                        newMetric.setMinLimit(60.0);
                        newMetric.setMaxLimit(80.0);
                        newMetric.setThemeColor("#6366f1"); 
                        break;
                    case HEART_RATE:
                        newMetric.setUnit("BPM");
                        newMetric.setDescription("Number of times the heart beats per minute.");
                        newMetric.setImgUrl("https://images.unsplash.com/photo-1505503693641-1926193e8d57?w=500&auto=format&fit=crop&q=60");
                        newMetric.setMinLimit(60.0);
                        newMetric.setMaxLimit(100.0);
                        newMetric.setThemeColor("#ef4444");
                        break;
                    case SpO2_LEVEL:
                        newMetric.setUnit("%");
                        newMetric.setDescription("Oxygen saturation level in the blood.");
                        newMetric.setImgUrl("https://images.unsplash.com/photo-1584308666744-24d59b298b17?w=500&auto=format&fit=crop&q=60");
                        newMetric.setMinLimit(95.0);
                        newMetric.setMaxLimit(100.0);
                        newMetric.setThemeColor("#06b6d4");
                        break;
                    case BMI:
                        newMetric.setUnit("");
                        newMetric.setDescription("Estimate total body fat and determine if they are at a healthy weight");
                        newMetric.setImgUrl("https://plus.unsplash.com/premium_photo-1681400641919-d5d03f6c0720?w=500&auto=format&fit=crop&q=60");
                        newMetric.setMinLimit(18.5);
                        newMetric.setMaxLimit(24.9);
                        newMetric.setThemeColor("#22c55e"); 
                        break;
                    default:
                        newMetric.setUnit("Unknown");
                        newMetric.setDescription("Custom metric.");
                        newMetric.setMinLimit(0.0);
                        newMetric.setMaxLimit(100.0);
                        newMetric.setThemeColor("#6b7280"); 
                }
                
                healthMetricRepository.save(newMetric);
                System.out.println("Initialized default metric: " + type.name());
            }
        }
    }

    @Override
    public List<Metric> getAllActiveMetrics() {
        return healthMetricRepository.findAll().stream()
                .filter(Metric::isActive)
                .toList();
    }

    @Override
    public List<Metric> getAllMetrics() {
       return healthMetricRepository.findAll();
    }

    @Override
    public Metric addMetric(MetricRequest request) {
        Metric m = new Metric();
        m.setName(request.getName().toUpperCase().replace(" ", "_"));
        m.setUnit(request.getUnit());
        m.setDescription(request.getDescription());
        m.setImgUrl(request.getImgUrl());
        m.setMinLimit(request.getMinLimit());
        m.setMaxLimit(request.getMaxLimit());
        m.setThemeColor(request.getThemeColor());
        m.setActive(true);
        return healthMetricRepository.save(m);
    }

    @Override
    public Metric toggleMetricStatus(Long id) {
       Metric m = healthMetricRepository.findById(id).orElseThrow(() -> new RuntimeException("Metric not found"));
        m.setActive(!m.isActive());
        return healthMetricRepository.save(m);
    }

    @Override
    public Metric updateMetric(Long id, MetricRequest request) {
        Metric m = healthMetricRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Metric not found"));
        
        if (request.getName() != null) m.setName(request.getName().toUpperCase().replace(" ", "_"));
        if (request.getUnit() != null) m.setUnit(request.getUnit());
        if (request.getDescription() != null) m.setDescription(request.getDescription());
        if (request.getImgUrl() != null) m.setImgUrl(request.getImgUrl());
        if (request.getMinLimit() != null) m.setMinLimit(request.getMinLimit());
        if (request.getMaxLimit() != null) m.setMaxLimit(request.getMaxLimit());
        if (request.getThemeColor() != null) m.setThemeColor(request.getThemeColor());
        
        return healthMetricRepository.save(m);
    }
}