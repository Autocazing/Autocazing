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
    private Integer ingredientId;
    private Integer storeId;
    private Integer vendorId;
    private String ingredientName;
    private Integer ingredientPrice;
    private Integer ingredientCapacity;
    private Integer scaleId;
    private Integer minimumCount;
    private Integer deliveryTime;
    private Integer orderCount;
}
