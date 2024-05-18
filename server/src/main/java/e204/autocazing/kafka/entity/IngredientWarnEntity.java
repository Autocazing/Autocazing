package e204.autocazing.kafka.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientWarnEntity {
	private String type;
	private IngredientWarnInfoEntity ingredientWarnInfoEntity;

}
