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
@Table(name = "menuIngredient")
@IdClass(MenuIngredientId.class) //복합 키 클래스를 지정하기.
public class MenuIngredientEntity {
    @Id
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private MenuEntity menu;

    @Id
    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private IngredientEntity ingredient;

    @Column(nullable = false)
    private Integer capacity; //메뉴에 들어가는 재료의 양

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
