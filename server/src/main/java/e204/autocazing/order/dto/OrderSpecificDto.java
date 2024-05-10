package e204.autocazing.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderSpecificDto {
    private String menuName;
    private int menuQuantity;
    private int menuPrice;

}
