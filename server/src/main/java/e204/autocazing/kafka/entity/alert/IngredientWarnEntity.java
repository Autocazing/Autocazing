package e204.autocazing.kafka.entity.alert;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientWarnEntity {
	private String type;
	private IngredientWarnInfoEntity ingredientWarnInfo;

}
