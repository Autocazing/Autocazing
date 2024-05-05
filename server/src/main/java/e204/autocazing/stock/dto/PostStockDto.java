package e204.autocazing.stock.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostStockDto {

    private Integer quantity;
    private LocalDate expirationDate;
    private Integer ingredientId;

}
