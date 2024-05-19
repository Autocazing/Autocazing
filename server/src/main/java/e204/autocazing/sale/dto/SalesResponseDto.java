package e204.autocazing.sale.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalesResponseDto {
	private String type; // "day", "week", "month"
	private String date;
	private Integer week;
	private Integer month;
	private Integer year;
	private Integer totalSales;
}
