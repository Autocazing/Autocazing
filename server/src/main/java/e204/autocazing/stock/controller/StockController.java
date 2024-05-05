package e204.autocazing.stock.controller;

import e204.autocazing.stock.dto.PostStockDto;
import e204.autocazing.stock.dto.StockDetailsDto;
import e204.autocazing.stock.dto.UpdateStockDto;
import e204.autocazing.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    @PostMapping("")
    public ResponseEntity createStock(@RequestBody PostStockDto postStockDto){
        stockService.createStock(postStockDto);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }


    // 전체 재고 조회
    @GetMapping("")
    public ResponseEntity<List<StockDetailsDto>> getAllStocks() {
        List<StockDetailsDto> stocks = stockService.findAllStocks();
        return ResponseEntity.ok(stocks);
    }

    // 재고 상세 조회
    @GetMapping("/{stockId}")
    public ResponseEntity<StockDetailsDto> getStockById(@PathVariable(name = "stockId") Integer stockId) {
        StockDetailsDto stock = stockService.findStockById(stockId);
        return ResponseEntity.ok(stock);
    }

    // 재고 수정
    @PutMapping("/{stockId}")
    public ResponseEntity<StockDetailsDto> updateStock(@PathVariable(name = "stockId") Integer stockId, @RequestBody UpdateStockDto stockDto) {
        StockDetailsDto updatedStock = stockService.updateStock(stockId, stockDto);
        return ResponseEntity.ok(updatedStock);
    }

    // 재고 삭제
    @DeleteMapping("/{stockId}")
    public ResponseEntity<Void> deleteStock(@PathVariable(name = "stockId") Integer stockId) {
        stockService.deleteStock(stockId);
        return ResponseEntity.ok().build();
    }

}
