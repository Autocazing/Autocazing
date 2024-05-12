package e204.autocazing.restock.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//상세조회의 재료
public class RestockOrderSpecificDetailDto {
    private Integer restockOrderSpecificId;
    private String ingredientName;
    private Integer ingredientQuanrtity; // 용량
    private Double ingredientPrice; // 가격
    private String venderName; // 발주 업체 이름
    private Integer deliveryTime; // 배송 소요기간
}
