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
@Table(name = "ingredients")
public class IngredientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ingredientId;

    @Column(nullable = false , length = 20)
    private String ingredientName;

    @Column(nullable = false)
    private Integer ingredientPrice;

    @Column(nullable = false)
    private Integer ingredientCapacity;

    @Column(nullable = false)
    private Integer minimumCount;

    @Column(nullable = false)
    private Integer deliveryTime;

    @Column(nullable = false)
    private Integer orderCount;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private String imageUrl =" ";

    @OneToMany(mappedBy = "ingredient" , cascade = CascadeType.ALL)
    private List<MenuIngredientEntity> menuIngredients;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scale_id", nullable = false)
    private IngredientScaleEntity scale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private StoreEntity store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vender_id", nullable = false)
    private VenderEntity vender;

    @OneToOne(mappedBy = "ingredient")
    private StockEntity stock;
}
