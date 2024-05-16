package com.e204.autocazing_alert.kafka.entity;
import lombok.*;

@Data
public class IngredientWarnEntity extends ConsumerEntity {

	private Integer ingredientId;	// 발주를 넣을 재료의 id

}