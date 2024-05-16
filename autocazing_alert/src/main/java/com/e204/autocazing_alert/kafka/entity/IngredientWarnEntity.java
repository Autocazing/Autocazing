package com.e204.autocazing_alert.kafka.entity;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientWarnEntity extends ConsumerEntity {

	private Integer restockOrderId;
	private Integer ingredientId;

}