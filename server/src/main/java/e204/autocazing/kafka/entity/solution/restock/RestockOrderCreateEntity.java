package e204.autocazing.kafka.entity.solution.restock;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestockOrderCreateEntity {

    private Integer restockOrderId;
    private List<KafkaRestockOrderSpecific> restockOrderSpecifics;
    private String status;

}
