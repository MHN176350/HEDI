package com.group.thr.hedi.Entity;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.group.thr.hedi.Enum.MetricType;

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
   @Column(nullable = true)
   private String imgUrl;
}