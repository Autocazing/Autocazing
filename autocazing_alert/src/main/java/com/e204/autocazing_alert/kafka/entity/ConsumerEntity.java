package com.e204.autocazing_alert.kafka.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class ConsumerEntity {

    private String type;    // INGREDIENT_WARN, SALES, DELIVERY
    private String message;

}
