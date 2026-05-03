package com.group.thr.hedi.Entity;

import java.time.LocalDateTime;


import com.group.thr.hedi.Enum.NotificationType;

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
@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
@Id
@GeneratedValue( strategy =  GenerationType.IDENTITY)
private Long id;
@Column(nullable = false)
private NotificationType type;
@Column(nullable = false)
private String message;
@Column(nullable = false)
private boolean isRead=false;
@Column(name = "created_at", updatable = false)
private LocalDateTime createdAt;
@ManyToOne(fetch=FetchType.LAZY)
private User user;
}
