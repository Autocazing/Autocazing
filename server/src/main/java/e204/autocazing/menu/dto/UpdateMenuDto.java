package e204.autocazing.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UpdateMenuDto {
    private String menuName;
    private Integer menuPrice;
    private Boolean onEvent;
    private int discountRate;
    private String imageUrl;
    private List<MenuIngredientDto> ingredients;
    private Boolean soldOut;

}
