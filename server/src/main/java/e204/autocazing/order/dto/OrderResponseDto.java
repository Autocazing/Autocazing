package e204.autocazing.order.dto;

import e204.autocazing.db.entity.OrderEntity;
import e204.autocazing.db.entity.OrderSpecific;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderResponseDto {

    private Integer orderId;
    private List<OrderSpecificDto> orderSpecificDtos;

}
