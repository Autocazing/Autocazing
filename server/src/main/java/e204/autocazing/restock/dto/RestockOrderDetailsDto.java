package e204.autocazing.restock.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
//발주 상세조회용
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestockOrderDetailsDto {
//    private Integer restockOrderId;
    private List<RestockOrderSpecificDetailDto> specifics; // 발주된 재료들의 상세 정보
}
