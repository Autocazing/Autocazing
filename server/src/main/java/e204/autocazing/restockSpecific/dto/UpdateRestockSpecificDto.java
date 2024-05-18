package e204.autocazing.restockSpecific.dto;

import e204.autocazing.db.entity.RestockOrderSpecificEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRestockSpecificDto {

    private Integer ingredientQuantity;
    private RestockOrderSpecificEntity.RestockSpecificStatus status;

}
