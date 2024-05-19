package e204.autocazing.restock.dto;

import e204.autocazing.db.entity.RestockOrderSpecificEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//발주 수정 재료 dto  << 수정할수도..
public class UpdatedRestockSpecificDto {
    private Integer restockOrderSpecificId;
    private Integer ingredientQuantity;
    private Integer ingredientPrice;
    private String ingredientName;
    private Integer ingredientId;
    private RestockOrderSpecificEntity.RestockSpecificStatus restockSpecificStatus;
}
