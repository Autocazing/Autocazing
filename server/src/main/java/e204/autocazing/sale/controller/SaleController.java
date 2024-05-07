package e204.autocazing.sale.controller;

import java.util.List;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import e204.autocazing.sale.dto.saleDto;
import e204.autocazing.sale.service.SaleService;

@RestController
@RequestMapping("/sales")
public class SaleController {
	//월별 매출 : 12달
	//주별 매출 : 12주
	//일별 매출 : 30일

	@Autowired
	private SaleService saleService;

	@GetMapping("")
	public ResponseEntity getSales(@RequestParam("type") String type){
		List<saleDto> sales = saleService.getSales(type);
		return ResponseEntity.ok(sales);
	}
}
