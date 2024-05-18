package e204.autocazing.stock.controller;

import e204.autocazing.stock.dto.NearExpiredDto;
import e204.autocazing.stock.dto.PostStockDto;
import e204.autocazing.stock.dto.StockDetailsDto;
import e204.autocazing.stock.dto.UpdateStockDto;
import e204.autocazing.stock.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("api/stocks")
public class StockController {

    @Autowired
    private StockService stockService;


    @Operation(summary = "재고 추가 요청", description = "재고 추가(재료)요청을 수행하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "재고 추가 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = PostStockDto.class)))
                    )
    })
    @PostMapping("")
    public ResponseEntity<Void> createStock(@RequestBody List<PostStockDto> postStockDtos, HttpServletRequest httpServletRequest){
        String loginId = httpServletRequest.getHeader("loginId");
        stockService.createStock(postStockDtos,loginId);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }


    // 전체 재고 조회
    @Operation(summary = "재고 전체 조회", description = "재고 전체조회를 수행하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "재고 전체 조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = StockDetailsDto.class)))

            )
    })
    @GetMapping("")
    public ResponseEntity<List<StockDetailsDto>> getAllStocks(HttpServletRequest httpServletRequest) {
        String loginId = httpServletRequest.getHeader("loginId");
        System.out.println("request : " + httpServletRequest.toString());
        System.out.println("loginId :" + loginId );
        List<StockDetailsDto> stocks = stockService.findAllStocks(loginId);
        return ResponseEntity.ok(stocks);
    }


    // 재고 상세 조회
    @Operation(summary = "재고 상세 조회", description = "재고 상세조회를 수행하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "재고 상세조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StockDetailsDto.class)
                    )
            )
    })
    @GetMapping("/{stockId}")
    public ResponseEntity<StockDetailsDto> getStockById(@PathVariable(name = "stockId") Integer stockId) {
        StockDetailsDto stock = stockService.findStockById(stockId);
        return ResponseEntity.ok(stock);
    }

    // 재고 수정
    @Operation(summary = "재고 수정", description = "재고 수정을 수행하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "재고 수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StockDetailsDto.class)
                    )
            )
    })
    @PutMapping("/{stockId}")
    public ResponseEntity<StockDetailsDto> updateStock(@PathVariable(name = "stockId") Integer stockId, @RequestBody UpdateStockDto stockDto) {
        StockDetailsDto updatedStock = stockService.updateStock(stockId, stockDto);
        return ResponseEntity.ok(updatedStock);
    }


    // 재고 삭제
    @Operation(summary = "재고 삭제", description = "재고의 삭제 요청을 수행하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "재고 삭제 성공",
                    content = @Content(examples = {
                            @ExampleObject(
                                    name = "Stock 삭제 ",
                                    summary = "Stock 삭제 body의 예시",
                                    value = " "
                            )
                    })
            )
    })
    @DeleteMapping("/{stockId}")
    public ResponseEntity<Void> deleteStock(@PathVariable(name = "stockId") Integer stockId) {
        stockService.deleteStock(stockId);
        return ResponseEntity.ok().build();
    }

    //유통기한 임박상품 조회


    @GetMapping("/schedule")
    public ResponseEntity<HashMap<String,List<NearExpiredDto>>> test(){
        HashMap<String,List<NearExpiredDto>> list = stockService.checkAndOrderNearExpiryProducts();
        return ResponseEntity.ok(list);
    }

}
