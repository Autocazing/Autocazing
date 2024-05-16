package e204.autocazing.restock.controller;

import e204.autocazing.db.entity.OrderSpecific;
import e204.autocazing.db.entity.RestockOrderEntity;
import e204.autocazing.db.entity.RestockOrderSpecificEntity;
import e204.autocazing.restock.dto.*;
import e204.autocazing.restock.service.RestockOrderService;
import e204.autocazing.restockSpecific.dto.RestockSpecificResponseDto;
import e204.autocazing.restockSpecific.dto.UpdateRestockSpecificDto;
import e204.autocazing.restockSpecific.service.RestockSpecificService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;

import jakarta.ws.rs.QueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/restocks")
public class RestockController {
    @Autowired
    private RestockOrderService restockOrderService;
    @Autowired
    private RestockSpecificService restockSpecificService;

    @Hidden
    @PostMapping("")
    public ResponseEntity createRestockOrder(@RequestBody PostRestockDto postRestockDto, HttpServletRequest httpServletRequest) {
        String loginId = httpServletRequest.getHeader("loginId");

        restockOrderService.createRestockOrder(postRestockDto, loginId);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @Operation(summary = " 발주(장바구니) 재료 조회", description = "발주리스트를  status 에 따라 전체조회하는 API입니다."
                                                               +"status = WRITING 일때는 장바구니 조회"
                                                               +"status = null 일땐 진행중인 발주 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "발주 재료 상세조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RestockOrderDetailsDto.class)
                    )
            )
    })
    @GetMapping("")
    public ResponseEntity findRestock(@RequestParam(name = "status", required = false) RestockOrderEntity.RestockStatus status,
                                      HttpServletRequest httpServletRequest) {
        String loginId = httpServletRequest.getHeader("loginId");
        List<RestockOrderDetailsDto> restockOrderDetailsDtos =restockOrderService.findRestockOrderById(status,loginId);
        return ResponseEntity.ok(restockOrderDetailsDtos);
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
    @PostMapping("/specifics")
    public ResponseEntity addIngredientToRestockOrder(@RequestBody AddSpecificRequest addDto,
                                                      HttpServletRequest httpServletRequest) {
        String loginId = httpServletRequest.getHeader("loginId");
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
    @PutMapping("/specifics/{specificId}")
    public ResponseEntity updateRestockOrderSpecific(@PathVariable Integer specificId,
                                                     @RequestBody UpdateRestockSpecificDto updateRestockSpecificDto){
        RestockSpecificResponseDto restockSpecificResponseDto = restockSpecificService.updateRestockOrderSpecific(specificId,updateRestockSpecificDto);
        return ResponseEntity.ok(restockSpecificResponseDto);
    }

    @Operation(summary = "장바구니 재료 삭제 요청", description = "장바구니 재료 삭제를 수행하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "재료 삭제 성공")
    })
    //발주 재료 삭제
    @DeleteMapping("/specifics/{specificId}")
    public ResponseEntity<Void> deleteRestockOrderSpecific(@PathVariable Integer restockOrderId,
                                                           @PathVariable Integer restockOrderSpecificId) {
        restockSpecificService.deleteRestockOrderSpecific(restockOrderId,restockOrderSpecificId);
        return ResponseEntity.ok().build();
    }


}
