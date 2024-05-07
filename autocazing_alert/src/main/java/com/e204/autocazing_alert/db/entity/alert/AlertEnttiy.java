package com.e204.autocazing_alert.db.entity.alert;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "alerts")
public class AlertEnttiy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer alertId;

    @Column(nullable = false)
    private Integer storeId;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private Boolean completed;

    @Column(nullable = false)
    private Timestamp createdAt;
    @Column(nullable = false)
    private Timestamp updatedAt;

    @Builder
    public AlertEnttiy(Integer storeId, String content, Boolean completed) {
        this.storeId = storeId;
        this.content = content;
        this.completed = completed;
    }

    @PrePersist
    protected void onCreate() {	// 데이터베이스에 저장되기 전 시점에 실행되는 메서드
        Timestamp now = new Timestamp(System.currentTimeMillis());	// 그때의 시간을 받아옴
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {	// 엔티티가 업데이트 되는 시점에 실행되는 메서드
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }

}
