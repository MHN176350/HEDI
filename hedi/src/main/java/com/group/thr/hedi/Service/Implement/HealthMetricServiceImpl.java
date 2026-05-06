package com.group.thr.hedi.Service.Implement;

import com.group.thr.hedi.DAO.Interface.IHealthMetricDAO;
import com.group.thr.hedi.Entity.HealthRecord.MetricType;
import com.group.thr.hedi.Entity.Metric;
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
                        break;
                    case BLOOD_PRESSURE_SYSTOLIC:
                        newMetric.setUnit("mmHg");
                        newMetric.setDescription("Pressure in arteries when the heart beats.");
                        break;
                    case BLOOD_PRESSURE_DIASTOLIC:
                        newMetric.setUnit("mmHg");
                        newMetric.setDescription("Pressure in arteries between heartbeats.");
                        break;
                    case HEART_RATE:
                        newMetric.setUnit("BPM");
                        newMetric.setDescription("Number of times the heart beats per minute.");
                        break;
                    case SpO2_LEVEL:
                        newMetric.setUnit("%");
                        newMetric.setDescription("Oxygen saturation level in the blood.");
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