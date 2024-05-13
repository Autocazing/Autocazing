package e204.autocazing.menu.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuDto {
    private Integer menuId;
    private String menuName;
    private Integer menuPrice;
    private Boolean onEvent;
    private int discountRate;
    private String imageUrl;
    private Integer storeId;  // 가게 ID만 포함
    private List<MenuIngredientDto> ingredients;

}
