package e204.autocazing.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer stockId;
    @Column(nullable = false)
    private Integer quantity;
    @Column(nullable = false)
    private LocalDate expirationDate;


//    연관 관련
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "ingredient_id", nullable = false)
//    private IngredientEntity ingredient;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "vendor_id", nullable = false)
//    private VendorEntity vendor;
}
