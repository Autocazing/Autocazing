package e204.autocazing.restock.controller;

import e204.autocazing.db.entity.RestockOrderEntity;
import e204.autocazing.restock.dto.PostRestockDto;
import e204.autocazing.restock.dto.RestockDetailsDto;
import e204.autocazing.restock.dto.RestockOrderStatusDto;
import e204.autocazing.restock.dto.UpdateRestockDto;
import e204.autocazing.restock.service.RestockOrderService;
import e204.autocazing.stock.dto.PostStockDto;
import e204.autocazing.stock.dto.StockDetailsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/restocks")
public class RestockController {
    @Autowired
    private RestockOrderService restockOrderService;

    @Operation(summary = "장바구니 생성 요청", description = "장바구니 생성 요청을 수행하는 API입니다. Backend에서 호출.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "장바구니 생성 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostRestockDto.class)
                    )
            )
    })
    @PostMapping("")
    public ResponseEntity createRestockOrder(@RequestBody PostRestockDto postRestockDto) {
        restockOrderService.createRestockOrder(postRestockDto);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    // 전체 발주리스트 조회
    @Operation(summary = "발주리스트 전체 조회", description = "발주리스트를  status 에 따라 전체조회하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "발주리스트 전체 조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = RestockDetailsDto.class)))

            )
    })
    @GetMapping("")
    public ResponseEntity getAllRestockOrders(@RequestParam(value = "status", required = false)
                                                  @Schema(description = "발주 상태 필터",
                                                          example = "WRITING, ORDERED,ON_DELIVERY,ARRIVED,COMPLETE" // 예시를 보여주는 용도
                                                          ) // 가능한 ENUM 값들
                                                  List<RestockOrderStatusDto> status) {
        List<RestockDetailsDto> restocks = restockOrderService.findAllRestockOrders(status);
        return ResponseEntity.ok(restocks);
    }



    @Operation(summary = "발주리스트 상세 조회", description = "발주리스트 상세조회를 수행하  는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "발주 상세조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RestockDetailsDto.class)
                    )
            )
    })
    @GetMapping("/{restockOrderId}")
    public ResponseEntity getRestockOrderById(@PathVariable(name = "restockOrderId") Integer restockOrderId) {
        RestockDetailsDto restockDetailsDto =restockOrderService.findRestockOrderById(restockOrderId);
        return ResponseEntity.ok(restockDetailsDto);
    }

    // 재고 수정
    @Operation(summary = "발주리스트 수정", description = "발주리스트 수정을 수행하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "발주리스트 수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RestockDetailsDto.class)
                    )
            )
    })
    @PutMapping("/{restockOrderId}")
    public ResponseEntity updateRestockOrder(@PathVariable(name = "restockOrderId") Integer restockOrderId, @RequestBody UpdateRestockDto updateRestockDto) {
        RestockDetailsDto restockDetails = restockOrderService.updateRestockOrder(restockOrderId, updateRestockDto);
        return ResponseEntity.ok(restockDetails);
    }

    // 재고 삭제
    @Operation(summary = "발주 삭제", description = "발주 삭제 요청을 수행하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "발주 삭제 성공",
                    content = @Content(examples = {
                            @ExampleObject(
                                    name = "Restock 삭제 ",
                                    summary = "Restock 삭제 body의 예시",
                                    value = " "
                            )
                    })
            )
    })
    @DeleteMapping("/{restockOrderId}")
    public ResponseEntity deleteRestockOrder(@PathVariable(name = "restockOrderId") Integer restockOrderId) {
        restockOrderService.deleteRestockOrder(restockOrderId);
        return ResponseEntity.ok().build();
    }
}
