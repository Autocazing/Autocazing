package e204.autocazing.ingredient.dto;

import jakarta.validation.GroupSequence;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScaleDto {

    private Integer scaleId;
    private String unit;
}
