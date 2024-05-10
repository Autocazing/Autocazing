package e204.autocazing.restock.controller;

import e204.autocazing.db.entity.RestockOrderEntity;
import e204.autocazing.restock.dto.*;
import e204.autocazing.restock.service.RestockOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
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

    @Operation(summary = "장바구니 생성 요청", description = "장바구니 생성 요청을 수행하는 API입니다. Backend에서 호출(Front 호출 X).")
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
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = RestockOrderResponse.class)))

            )
    })
    @GetMapping("")
    public ResponseEntity getAllRestockOrders(@RequestParam(value = "status", required = false)
                                                  @Schema(description = "발주 상태 필터 status 를 리스트로 보내면됩니다. defalut는 아무것도없을때",
                                                          example = "/api/restockOrders?status=ORDERED&status=ON_DELIVERY&status=ARRIVED" // 예시를 보여주는 용도
                                                          ) // 가능한 ENUM 값들
                                                  List<RestockOrderEntity.RestockStatus> status) {
        List<RestockOrderResponse> restocks = restockOrderService.findAllRestockOrders(status);
        return ResponseEntity.ok(restocks);
    }



    @Operation(summary = "발주리스트 상세 조회", description = "발주리스트 상세조회를 수행하  는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "발주 상세조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RestockOrderDetailsDto.class)
                    )
            )
    })
    @GetMapping("/{restockOrderId}")
    public ResponseEntity getRestockOrderById(@PathVariable(name = "restockOrderId") Integer restockOrderId) {
        RestockOrderDetailsDto restockOrderDetailsDto =restockOrderService.findRestockOrderById(restockOrderId);
        return ResponseEntity.ok(restockOrderDetailsDto);
    }

    // 발주 하기 및 새로운 장바구니 생성.
    @Operation(summary = "발주하기 및 새로운 장바구니 생성 / 발주완료", description = "발주하기-> 'status':'ORDERED' // status:'COMPLETE' 일때 발주완료(재고반영) ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "발주하기 성공 , 새로운 장바구니 생성",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UpdatedRestockDto.class)
                    )
            )
    })
    @PutMapping("/{restockOrderId}")
    public ResponseEntity updateRestockOrder(@PathVariable(name = "restockOrderId") Integer restockOrderId, @RequestBody UpdateRestockDto updateRestockDto) {
        UpdatedRestockDto restockDetails = restockOrderService.updateRestockOrderStatus(restockOrderId, updateRestockDto);
        return ResponseEntity.ok(restockDetails);
    }


    //수동 발주재료추가
    @PostMapping("/{restockOrderId}/add")
    public ResponseEntity addIngredientToRestockOrder(@PathVariable Integer restockOrderId,@RequestBody AddSpecificRequest addDto) {
        AddSpecificResponse addSpecific = restockOrderService.addSpecific(addDto);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }
}
