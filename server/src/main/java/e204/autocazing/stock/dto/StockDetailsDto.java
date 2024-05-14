package e204.autocazing.stock.dto;

import jakarta.annotation.security.DenyAll;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockDetailsDto {
    private Integer stockId;
    private Integer quantity;
    private LocalDate expirationDate;
    private Integer ingredientId;
    private String ingredientName;
    private Integer deliveringCount;


}
