package e204.autocazing.order.dto;

import e204.autocazing.db.entity.OrderSpecific;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetailOrderResponseDto {
    private Integer orderId;
    private List<OrderSpecific> orderDetails;


}
