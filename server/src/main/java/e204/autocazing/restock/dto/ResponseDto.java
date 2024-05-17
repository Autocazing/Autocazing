package e204.autocazing.restock.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto {
    private Integer restockOrderId;
    private List<RestockOrderSpecificDetailDto> specifics; // 발주된 재료들의 상세 정보
}
