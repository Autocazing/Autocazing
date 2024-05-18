package e204.autocazing.kafka.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientCreateEntity {

    private Integer ingredientId;
    private String ingredientName;
    private Integer ingredientPrice;
    private Integer orderCount;

}
