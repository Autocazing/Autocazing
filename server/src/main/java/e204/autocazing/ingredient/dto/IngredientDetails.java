package e204.autocazing.ingredient.dto;

import e204.autocazing.db.entity.IngredientScaleEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientDetails {
    private Integer ingredientId;
    private String ingredientName;
    private Integer venderId;
    private String venderName;
    private int ingredientPrice;
    private Integer ingredientCapacity;
    private ScaleDto scale;
    private Integer minimumCount;
    private Integer deliveryTime;
    private Integer orderCount;
    private String imageUrl;
}
