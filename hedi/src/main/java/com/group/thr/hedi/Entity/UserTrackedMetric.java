package com.group.thr.hedi.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_tracked_metrics")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserTrackedMetric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "metric_id", nullable = false)
    private Metric metric;

    @Column(nullable = false)
    private boolean isActive = true;

    @Column(nullable = true)
    private Double currentPersonalBaseline;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int consecutiveWarnings;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int consecutiveAlerts;

    @Column(length = 20)
    private String currentTrend;
}