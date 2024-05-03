package e204.autocazing.order.dto;

import e204.autocazing.db.entity.OrderEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderResponseDto {

    private Integer orderId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public static OrderResponseDto fromEntity(OrderEntity orderEntity) {
        return OrderResponseDto.builder()
                .orderId(orderEntity.getOrderId())
                .createdAt(orderEntity.getCreatedAt())
                .updatedAt(orderEntity.getUpdatedAt())
                .build();

    }
}
