package e204.autocazing.restock.dto;

import e204.autocazing.db.entity.RestockOrderEntity;
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
//발주 상태 수정
public class UpdatedRestockDto {

    private Integer restockOrderId;
    private RestockOrderEntity.RestockStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<UpdatedRestockSpecificDto> updatedRestockSpecificDtoList;
}
