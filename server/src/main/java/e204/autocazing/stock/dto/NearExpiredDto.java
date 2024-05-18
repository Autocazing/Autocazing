package e204.autocazing.stock.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NearExpiredDto {

    private Integer ingredientId;
    private String ingredientName;
    private Integer quantity;

}
