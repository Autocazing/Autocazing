package e204.autocazing.ingredient.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientDto {

    private Integer ingredientId;
    private Integer storeId;
    private Integer vendorId;
    private String ingredientName;
    private int ingredientPrice;
    private Integer ingredientCapacity;
    private Integer scaleId;
    private Integer minimumCount;
    private Integer deliveryTime;
    private Integer orderCount;
    private String imageUrl;
}
