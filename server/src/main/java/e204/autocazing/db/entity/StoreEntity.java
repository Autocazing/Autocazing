package e204.autocazing.db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "store")

public class StoreEntity {
    //기본키
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer StoreId;
    //로그인 Id
    @Column(nullable = false)
    private String loginId;
    //비밀번호
    @Column(nullable = false)
    private String password;
    //가게 이름
    @Column(nullable = false)
    private String storeName;

    //생성 시간
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    //수정 시간
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

//    @OneToMany(mappedBy = "storeEntity")
//    private List<Order> orders;
}
