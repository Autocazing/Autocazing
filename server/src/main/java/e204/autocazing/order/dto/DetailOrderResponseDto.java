package e204.autocazing.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class DetailOrderResponseDto {
    private Integer orderId;
    private List<OrderDetailDto> orderDetails;


    @Getter
    @Setter
    public static class OrderDetailDto {
        private Integer menuId;
        private int quantity;
        private int price;


    }
}
