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
public class PostMenuDto {
    private String menuName;
    private Double menuPrice;
    private Boolean onEvent;
    private List<MenuIngredientDto> ingredients;
}
