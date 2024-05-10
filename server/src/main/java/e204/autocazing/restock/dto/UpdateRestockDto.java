package e204.autocazing.restock.dto;

import e204.autocazing.db.entity.RestockOrderEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRestockDto {

    private RestockOrderEntity.RestockStatus status;

}
