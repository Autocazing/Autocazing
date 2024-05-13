package com.e204.autocazing_alert.db.entity.report;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.sql.Timestamp;
import java.util.List;

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
    private Integer sales;  // 익일 일매출

    @Column(nullable = false)
    private Integer expectedMonthlySales;   // 현월의 예상 월매출(딥러닝 예측 기반)
    @Column(nullable = false)
    private Integer currentMonthlySales;    // 현재까지 달성한 월매출

    @ElementCollection
    @CollectionTable(name = "menuSpecifics", joinColumns = @JoinColumn(name="reportId", referencedColumnName = "reportId"))
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Integer> menuSpecific; // 매출에 문제가 생긴 메뉴들의 id

    @ElementCollection
    @CollectionTable(name = "expirationSpecifics", joinColumns = @JoinColumn(name="reportId", referencedColumnName = "reportId"))
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Integer> expirationSpecific;   // 유통기한 만료되는 재료들의 id

    @Column(nullable = false)
    private Timestamp createdAt;
    @Column(nullable = false)
    private Timestamp updatedAt;

    @Builder
    public ReportEntity(Integer storeId, Integer sales, Integer expectedMonthlySales, Integer currentMonthlySales, List<Integer> menuSpecific, List<Integer> expirationSpecific) {
        this.storeId = storeId;
        this.sales = sales;
        this.expectedMonthlySales = expectedMonthlySales;
        this.currentMonthlySales = currentMonthlySales;
        this.menuSpecific = menuSpecific;
        this.expirationSpecific = expirationSpecific;
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
