package e204.autocazing.restock.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//발주에 재료 등록 Dto
public class AddSpecificRequest {
//    private Integer restockOrderId; // 재료를 추가할 발주 ID
    private Integer ingredientId;
    private Integer ingredientQuantity; // 재료의 수량
}
