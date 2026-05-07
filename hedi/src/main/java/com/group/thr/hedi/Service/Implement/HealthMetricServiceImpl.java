package com.group.thr.hedi.Service.Implement;

import com.group.thr.hedi.DAO.Interface.IHealthMetricDAO;
import com.group.thr.hedi.Entity.Metric;
import com.group.thr.hedi.Enum.MetricType;
import com.group.thr.hedi.Service.Interface.IHealthMetricService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HealthMetricServiceImpl implements IHealthMetricService {

    @Autowired
    private IHealthMetricDAO healthMetricDAO;

    @Override
    @Transactional
    public void initializeDefaultMetrics() {
        for (MetricType type : MetricType.values()) {

            boolean exists = healthMetricDAO.findAll().stream()
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
                        break;
                    case BLOOD_PRESSURE_SYSTOLIC:
                        newMetric.setUnit("mmHg");
                        newMetric.setDescription("Pressure in arteries when the heart beats.");
                        newMetric.setImgUrl("https://images.unsplash.com/photo-1631549916768-4119b2e5f926?w=500&auto=format&fit=crop&q=60");
                        break;
                    case BLOOD_PRESSURE_DIASTOLIC:
                        newMetric.setUnit("mmHg");
                        newMetric.setDescription("Pressure in arteries between heartbeats.");
                        newMetric.setImgUrl("https://images.unsplash.com/photo-1584017911766-d451b3d0e843?w=500&auto=format&fit=crop&q=60");
                        break;
                    case HEART_RATE:
                        newMetric.setUnit("BPM");
                        newMetric.setDescription("Number of times the heart beats per minute.");
                        newMetric.setImgUrl("https://images.unsplash.com/photo-1505503693641-1926193e8d57?w=500&auto=format&fit=crop&q=60");
                        break;
                    case SpO2_LEVEL:
                        newMetric.setUnit("%");
                        newMetric.setDescription("Oxygen saturation level in the blood.");
                        newMetric.setImgUrl("https://images.unsplash.com/photo-1584308666744-24d59b298b17?w=500&auto=format&fit=crop&q=60");
                        break;
                    case BMI:
                        newMetric.setUnit("");
                        newMetric.setDescription("Estimate total body fat and determine if they are at a healthy weight");
                        newMetric.setImgUrl("https://plus.unsplash.com/premium_photo-1681400641919-d5d03f6c0720?w=500&auto=format&fit=crop&q=60");
                        break;
                    default:
                        newMetric.setUnit("Unknown");
                        newMetric.setDescription("Custom metric.");
                }
                
                healthMetricDAO.save(newMetric);
                System.out.println("Initialized default metric: " + type.name());
            }
        }
    }

@Override
public List<Metric> getAllActiveMetrics() {
    return healthMetricDAO.findAll().stream()
            .filter(Metric::isActive)
            .toList();
}
}