package e204.autocazing.sale.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import e204.autocazing.sale.dto.SalesResponseDto;
import e204.autocazing.sale.service.SaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("api/sales")
public class SaleController {

	@Autowired
	private SaleService saleService;

	@Operation(summary = "일별, 주별, 월별 매출 조회 요청", description = "요청일을 포함한 일별, 주별, 월별 매출 조회를 수행하는 API입니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Sales data retrieved successfully",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = SalesResponseDto.class),
				examples = {
					@ExampleObject(
						name = "Daily Sales Example",
						description = "Example of daily sales data retrieval",
						value = "[{\"type\": \"day\", \"date\": \"2024-04-07\", \"totalSales\": 271210}]"
					),
					@ExampleObject(
						name = "Weekly Sales Example",
						description = "Example of weekly sales data retrieval",
						value = "[{\"type\": \"week\", \"week\": 7, \"year\": 2024, \"totalSales\": 3239693}]"
					),
					@ExampleObject(
						name = "Monthly Sales Example",
						description = "Example of monthly sales data retrieval",
						value = "[{\"type\": \"month\", \"month\": 5, \"year\": 2023, \"totalSales\": 12582632}]"
					)
				}
			)
		)
	})
	@GetMapping("")
	public ResponseEntity getSales(
		@Parameter(description = " 'day' 또는 'week' 또는 'month' 으로 요청할 수 있습니다.",
			required = true,
			schema = @Schema(type = "string", allowableValues = {"day", "week", "month"}))
		@RequestParam("type") String type) { //type : 일별 day, 주별 week, 월별 month
		List<Map<String, Object>> sales = saleService.getSales(type);
		return ResponseEntity.ok(sales);
	}

	@Operation(summary = "금일 판매 잔 수 조회 요청", description = "오늘 몇 잔 판매했는지를 조회하는 API입니다. ")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Sales data retrieved successfully",
			content = @Content(mediaType = "application/json",
				examples = {
					@ExampleObject(
						value = "35"
					)
				}
			)
		)
	})
	@GetMapping("/sold")
	public ResponseEntity getSoldNumber() {
		Integer soldNumber = saleService.getSoldNumber();
		return ResponseEntity.ok(soldNumber);
	}

	@Operation(summary = "요일별 평균 매출 요청", description = "요청일로부터 한 달 전까지 매출 데이터 기반 요일 별 평균 매출 조회 API입니다. ")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Sales data retrieved successfully",
			content = @Content(mediaType = "application/json",
				examples = {
					@ExampleObject(
						value = "{\"Monday\": 29392.14285714286,\n"
							+ "    \"Thursday\": 31351.634146341465,\n"
							+ "    \"Friday\": 31750.06358381503,\n"
							+ "    \"Sunday\": 32922.87804878049,\n"
							+ "    \"Wednesday\": 31548.51595744681,\n"
							+ "    \"Tuesday\": 31635.365853658535,\n"
							+ "    \"Saturday\": 32592.672514619884}"
					)
				}
			)
		)

	})
	@GetMapping("/avg")
	public ResponseEntity getAvgSales() {
		Map<String, Double> sales = saleService.getAvgSales();
		return ResponseEntity.ok(sales);
	}
}