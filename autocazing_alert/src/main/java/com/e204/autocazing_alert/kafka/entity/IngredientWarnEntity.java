package com.e204.autocazing_alert.kafka.entity;
import lombok.*;

@Data
@AllArgsConstructor
public class IngredientWarnEntity {

	private String type;
	private IngredientWarnInfoEntity ingredientWarnInfo;

}