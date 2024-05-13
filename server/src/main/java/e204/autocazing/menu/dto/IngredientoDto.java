package e204.autocazing.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngredientoDto {

    private Integer ingredientId;
    private String ingredientName;
    private Integer capacity;
}
