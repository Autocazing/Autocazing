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
@Table(name = "RestockOrderSpecifics")
public class RestockOrderSpecificEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer restockOrderSpecificId;

    @Column(nullable = false)
    private Integer ingredientQuantity;

    @Column(nullable = false)
    private Integer ingredientPrice;

    @Column(nullable = false)
    private String ingredientName;

    @Column(nullable = false)
    private Integer ingredientId;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restock_order_id", nullable = false)
    private RestockOrderEntity restockOrder;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RestockOrderSpecificEntity.RestockSpecificStatus status;

    public enum RestockSpecificStatus {
        WRITING, ORDERED, ON_DELIVERY, ARRIVED, COMPLETE
    }

    //굳이 연관?
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "ingredient_id", nullable = false)
//    private IngredientEntity ingredient;

}
