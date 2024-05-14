package e204.autocazing.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "RestockOrders")
public class RestockOrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer restockOrderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RestockStatus status=RestockStatus.WRITING;  // 기본값으로 WRITING (작성중)

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private StoreEntity store;

    @OneToMany(mappedBy = "restockOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RestockOrderSpecificEntity> restockOrderSpecific;

    public enum RestockStatus {
        WRITING, ORDERED, ON_DELIVERY, ARRIVED, COMPLETE
    }
}
