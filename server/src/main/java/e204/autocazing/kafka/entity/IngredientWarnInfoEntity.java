package e204.autocazing.kafka.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientWarnInfoEntity {

    private Integer restockOrderId;
    private Integer ingredientId;
    private Integer ingredientQuantity;

}
