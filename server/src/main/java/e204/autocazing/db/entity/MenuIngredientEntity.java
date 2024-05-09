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
public class MenuIngredientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    //연관
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "menu_id", referencedColumnName = "menuId")
//    private MenuEntity menu;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "ingredient_id", referencedColumnName = "ingredientId")
//    private IngredientEntity ingredient;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "vendor_id", referencedColumnName = "vendorId")
//    private VendorEntity vendor;
}
