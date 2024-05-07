package e204.autocazing.sale.dto;

import java.util.List;

import e204.autocazing.menu.dto.MenuIngredientDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class saleDto {
	private String MonthOrWeekOrDay;
	private Integer sales;
}
