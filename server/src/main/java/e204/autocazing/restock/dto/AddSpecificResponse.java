package e204.autocazing.restock.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddSpecificResponse {
    private String ingredientName;
    private Integer IngredientQuantity;
    private int ingredientPrice;
    private String venderName;
    private Integer deliveryTime;
}
