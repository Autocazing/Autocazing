package e204.autocazing.kafka.entity.solution.order;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KafkaOrderSpecific {

    private Integer menuId;
    private String menuName;
    private Integer menuQuantity;
    private Integer menuPrice;

}
