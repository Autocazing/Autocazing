package e204.autocazing.restockSpecific.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostRestockSpecificDto {
    private Integer ingredientId;
    private Integer restockOrderId;
    private Integer ingredientQuantity;
//    private Integer ingredientPrice;

}
