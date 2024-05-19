package e204.autocazing.kafka.entity.solution.restock;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KafkaRestockOrderSpecific {

    private String ingredientName;
    private Integer ingredientQuantity;
    private Integer ingredientPrice;
    private String restockSpecificStatus;

}
