package e204.autocazing.restock.controller;

import e204.autocazing.db.entity.RestockOrderEntity;
import e204.autocazing.restock.dto.*;
import e204.autocazing.restock.service.RestockOrderService;
import e204.autocazing.restockSpecific.dto.RestockSpecificResponseDto;
import e204.autocazing.restockSpecific.dto.UpdateRestockSpecificDto;
import e204.autocazing.restockSpecific.service.RestockSpecificService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;

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
    @Autowired
    private RestockSpecificService restockSpecificService;

    @Operation(summary = "장바구니 생성 요청", description = "장바구니 생성 요청을 수행하는 API입니다. Backend에서 호출(Front 호출 X).")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "장바구니 생성 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostRestockDto.class)
                    )
            )
    })
    @PostMapping("")
    public ResponseEntity createRestockOrder(@RequestBody PostRestockDto postRestockDto, HttpServletRequest httpServletRequest) {
        String loginId = httpServletRequest.getHeader("loginId");

        restockOrderService.createRestockOrder(postRestockDto, loginId);
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
                                                  @Parameter(description = "발주 상태 필터 status 를 리스트로 보내면됩니다. defalut는 아무것도없을때",
                                                      required = false,
                                                      schema = @Schema(type = "string", allowableValues = {"","WRITING", "ORDERED", "ARRIVED", "ON_DELIVERY", "COMPLETE"})) // 가능한 ENUM 값들
                                                  List<RestockOrderEntity.RestockStatus> status, HttpServletRequest httpServletRequest) {
        String loginId = httpServletRequest.getHeader("loginId");
        List<RestockOrderResponse> restocks = restockOrderService.findAllRestockOrders(status, loginId);
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
    public ResponseEntity updateRestockOrder(@PathVariable(name = "restockOrderId") Integer restockOrderId,
        @RequestBody UpdateRestockDto updateRestockDto, HttpServletRequest httpServletRequest) {
        String loginId = httpServletRequest.getHeader("loginId");
        UpdatedRestockDto restockDetails = restockOrderService.updateRestockOrderStatus(restockOrderId, updateRestockDto, loginId);
        return ResponseEntity.ok(restockDetails);
    }


    // 장바구니 내 재료 추가
    @Operation(summary = "장바구니 내 재료주문추가", description = "장바구니에서 수동으로 재료주문을 추가 ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "재료 추가 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AddSpecificResponse.class)
                    )
            )
    })
    //수동 발주재료추가
    @PostMapping("/{restockOrderId}/add")
    public ResponseEntity addIngredientToRestockOrder(@PathVariable Integer restockOrderId,@RequestBody AddSpecificRequest addDto) {
        AddSpecificResponse addSpecific = restockOrderService.addSpecific(addDto);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }




    @Operation(summary = "장바구니 재료 수정", description = "장바구니 재료 수정하는 API 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "재료 수정 완료",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RestockSpecificResponseDto.class)
                    )
            )
    })
    //발주 재료 수정
    @PutMapping("/{restockOrderId}/specifics/{specificId}")
    public ResponseEntity updateRestockOrderSpecific(@PathVariable Integer restockOrderId,
                                                     @PathVariable Integer specificId,
                                                     @RequestBody UpdateRestockSpecificDto updateRestockSpecificDto){
        RestockSpecificResponseDto restockSpecificResponseDto = restockSpecificService.updateRestockOrderSpecific(restockOrderId,specificId,updateRestockSpecificDto);
        return ResponseEntity.ok(restockSpecificResponseDto);
    }




    @Operation(summary = "장바구니 재료 삭제 요청", description = "장바구니 재료 삭제를 수행하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "재료 삭제 성공")
    })
    //발주 재료 삭제
    @DeleteMapping("/{restockOrderId}/specifics/{restockOrderSpecificId}")
    public ResponseEntity<Void> deleteRestockOrderSpecific(@PathVariable Integer restockOrderId,
                                                           @PathVariable Integer restockOrderSpecificId) {
        restockSpecificService.deleteRestockOrderSpecific(restockOrderId,restockOrderSpecificId);
        return ResponseEntity.ok().build();
    }
}
