package com.e204.autocazing_alert.kafka.entity;

import lombok.Data;

@Data
public abstract class ConsumerEntity {

    private String message;
    private String type;    // INGREDIENT_WARN, SALES, DELIVERY

}
