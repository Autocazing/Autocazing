package e204.autocazing.restock.dto;

import e204.autocazing.db.entity.RestockOrderEntity;
import e204.autocazing.db.entity.StoreEntity;
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
//발주 조회 response
public class RestockOrderResponse {

    private Integer restockOrderId;
    private RestockOrderEntity.RestockStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private StoreEntity store;
    private List<UpdatedRestockSpecificDto> restockOrderSpecific;
}
