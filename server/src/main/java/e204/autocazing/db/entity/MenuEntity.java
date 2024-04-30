package e204.autocazing.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MenuEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer menuId;
    @Column(nullable = false)
    private String menuName;
    @Column(nullable = false)
    private Integer menuPrice;
    @Column(nullable = false)
    private Boolean onEvent;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    //가게와 연관
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "store_id", nullable = false)
//    private StoreEntity store;
}
