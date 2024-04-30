package e204.autocazing.order.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {

    // 주문 상세 정보
    private List<OrderDetailDto> orderDetails;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class OrderDetailDto {
        private Integer menuId; // 메뉴 ID
        private int quantity; // 메뉴 수량
        private int price; // 메뉴 가격


    }
}
