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
public class PostRestockDto {
    private Integer storeId;
    private RestockOrderEntity.RestockStatus status = RestockOrderEntity.RestockStatus.WRITING;
//    private List<RestockOrderSpecificDTO> details

}
