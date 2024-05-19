package e204.autocazing.kafka.entity.alert;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientWarnInfoEntity {

    private Integer restockOrderId;
    private Integer ingredientId;
    private String ingredientName;
    private Integer orderCount;

}
