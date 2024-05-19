package e204.autocazing.kafka.entity.solution.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateEntity {

    private Integer orderId;
    private List<KafkaOrderSpecific> orderSpecifics;

}
