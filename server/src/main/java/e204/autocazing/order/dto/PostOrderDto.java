package e204.autocazing.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostOrderDto {
    private Integer storeId;
    private List<PostOrderSpecificDto> orderSpecifics;

}
