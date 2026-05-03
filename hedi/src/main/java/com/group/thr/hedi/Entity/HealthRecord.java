package com.group.thr.hedi.Entity;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "health_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetricType metricType;

    @Column(nullable = false)
    private Double metricValue;

    @Column(nullable = false)
    private LocalDateTime recordedAt;

    public enum MetricType {
        BLOOD_SUGAR,
        BLOOD_PRESSURE_SYSTOLIC,
        BLOOD_PRESSURE_DIASTOLIC,
        HEART_RATE,
        SpO2_LEVEL
    }
}