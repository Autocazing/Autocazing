package com.e204.autocazing_alert.db.entity.report;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "reports")
public class ReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reportId;

    @Column(nullable = false)
    private Integer storeId;
    @Column(nullable = false)
    private Integer sales;

    @Column(nullable = false)
    private Integer expectedMonthlySales;
    @Column(nullable = false)
    private Integer currentMonthlySales;

    @Column(nullable = false)
    private Timestamp createdAt;
    @Column(nullable = false)
    private Timestamp updatedAt;

    @Builder
    public ReportEntity(Integer storeId, Integer sales, Integer expectedMonthlySales, Integer currentMonthlySales) {
        this.storeId = storeId;
        this.sales = sales;
        this.expectedMonthlySales = expectedMonthlySales;
        this.currentMonthlySales = currentMonthlySales;
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
