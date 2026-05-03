package com.group.thr.hedi.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "thresholds")
@AllArgsConstructor
@NoArgsConstructor
public class Threshold {
@Id
@GeneratedValue(strategy =  GenerationType.IDENTITY)
private Long id;
@Column(nullable = false)
private double minValue;
@Column(nullable = false)
private double maxValue;
@ManyToOne(fetch = FetchType.LAZY)
private Metric metric;
@ManyToOne(fetch = FetchType.LAZY)
private User user;
@Column(nullable = false)
private boolean isActive;
}
