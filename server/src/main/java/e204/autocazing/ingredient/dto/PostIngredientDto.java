package e204.autocazing.ingredient.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostIngredientDto {

    private Integer venderId;

    private String ingredientName;

    private int ingredientPrice;

    private Integer ingredientCapacity;

    private ScaleDto scale;

    //    private Integer scaleId;
    private Integer minimumCount;

    private Integer deliveryTime;

    private Integer orderCount;

    private String imageUrl;
}
