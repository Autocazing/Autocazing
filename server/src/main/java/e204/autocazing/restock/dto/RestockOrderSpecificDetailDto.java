package e204.autocazing.restock.dto;

import e204.autocazing.db.entity.RestockOrderSpecificEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDate arrivedAt; //예상 도착시간
    private RestockOrderSpecificEntity.RestockSpecificStatus restockSpecificStatus;
}
