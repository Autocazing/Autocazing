package e204.autocazing.ingredient.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientDetails {
    private Integer ingredientId;
    private String ingredientName;
    private String venderName;
    private int ingredientPrice;
    private Integer ingredientCapacity;
    private String unit;
    private Integer minimumCount;
    private Integer deliveryTime;
    private Integer orderCount;
    private String imageUrl;
}
