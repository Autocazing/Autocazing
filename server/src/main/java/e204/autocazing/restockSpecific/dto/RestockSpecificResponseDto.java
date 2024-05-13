package e204.autocazing.restockSpecific.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestockSpecificResponseDto {
    private Integer restockOrderSpecificId;
    private Integer ingredientId;
    private String ingredientName;
    private Integer restockOrderId;
    private Integer ingredientQuantity;
    private Integer ingredientPrice;
    private String vendorName;
    private String status;
}

