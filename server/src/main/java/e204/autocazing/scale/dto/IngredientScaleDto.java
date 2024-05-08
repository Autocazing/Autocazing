package e204.autocazing.scale.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientScaleDto {

    private Integer scaleId;
    private String unit;

}