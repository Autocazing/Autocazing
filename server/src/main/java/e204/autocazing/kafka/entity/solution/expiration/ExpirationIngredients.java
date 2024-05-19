package e204.autocazing.kafka.entity.solution.expiration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpirationIngredients {

    private Integer ingredientId;
    private String ingredientName;
    private Integer remain;

}
