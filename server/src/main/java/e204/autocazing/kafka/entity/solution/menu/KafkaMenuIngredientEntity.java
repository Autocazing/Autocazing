package e204.autocazing.kafka.entity.solution.menu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KafkaMenuIngredientEntity {

    private Integer ingredientId;
    private Integer capacity;

}
