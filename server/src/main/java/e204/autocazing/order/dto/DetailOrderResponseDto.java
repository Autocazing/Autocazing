package e204.autocazing.order.dto;

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
    private List<OrderDetailDto> orderDetails;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderDetailDto {
        private Integer menuId;
        private int quantity;
        private int price;


    }
}
