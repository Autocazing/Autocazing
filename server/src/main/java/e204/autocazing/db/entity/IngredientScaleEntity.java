package e204.autocazing.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ingredientScales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngredientScaleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer scaleId;

    @Column(nullable = false, length = 10)
    private String unit;

    @Column(nullable = false)
    private Integer storeId;


}
