package e204.autocazing.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "orders")	// schema 설정 따로 x, public schema 내에 생성됨.

public class OrderEntity {
    //기본키
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer OrderId;
    //생성시간
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    //수정시간
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    //가게 ID 외래키설정
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "store_id")
//    private Store store;

}
