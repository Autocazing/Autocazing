package e204.autocazing.ingredient.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatchIngredientDto {
    private Integer venderId;
    private String ingredientName;
    private Integer ingredientPrice;
    private Integer ingredientCapacity;
    private ScaleDto scale;
    private Integer minimumCount;
    private Integer deliveryTime;
    private Integer orderCount;
    private String imageUrl;
}
