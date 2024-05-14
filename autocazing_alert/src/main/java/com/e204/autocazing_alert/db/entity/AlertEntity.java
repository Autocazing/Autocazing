package com.e204.autocazing_alert.db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "alerts")
public class AlertEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer alertId;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private String loginId;
    @Column(nullable = false)
    private Boolean completed;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;


}

