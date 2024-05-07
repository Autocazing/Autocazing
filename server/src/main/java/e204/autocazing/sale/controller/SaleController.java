package e204.autocazing.sale.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import e204.autocazing.sale.service.SaleService;

@RestController
@RequestMapping("api/sales")
public class SaleController {

	@Autowired
	private SaleService saleService;

	@GetMapping("")
	public ResponseEntity getSales(@RequestParam("type") String type){ //type : 일별 day, 주별 week, 월별 month
		List<Map<String, Object>> sales = saleService.getSales(type);
		return ResponseEntity.ok(sales);
	}
}
