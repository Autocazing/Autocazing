package e204.autocazing.kafka.entity.solution;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuCreateEntity {

    private Integer menuId;
    private String menuName;
    private Integer menuPrice;
    private Boolean onEvent;
    private Integer discountRate;
    private List<KafkaMenuIngredientEntity> menuIngredients;

}
