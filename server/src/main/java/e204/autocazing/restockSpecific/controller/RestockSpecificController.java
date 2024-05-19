package e204.autocazing.restockSpecific.controller;

import e204.autocazing.db.entity.RestockOrderEntity;
import e204.autocazing.db.entity.RestockOrderSpecificEntity;
import e204.autocazing.restock.dto.UpdateRestockDto;
import e204.autocazing.restock.dto.UpdatedRestockDto;
import e204.autocazing.restockSpecific.dto.PostRestockSpecificDto;
import e204.autocazing.restockSpecific.dto.RestockSpecificDto;
import e204.autocazing.restockSpecific.dto.RestockSpecificResponseDto;
import e204.autocazing.restockSpecific.dto.UpdateRestockSpecificDto;
import e204.autocazing.restockSpecific.service.RestockSpecificService;
import e204.autocazing.stock.dto.PostStockDto;
import e204.autocazing.stock.dto.StockDetailsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
@RequestMapping("api/restock-specifics")
public class RestockSpecificController {
    @Autowired
    private RestockSpecificService restockSpecificService;

    // 수정
//    @Operation(summary = "발주리스트 내 재료목록  수정", description = "발주리스트 내 재료목록 수정을 수행하는 API입니다.")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "발주리스트 내 재료목록 수정 수정 성공",
//                    content = @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = RestockSpecificDto.class)
//                    )
//            )
//    })
//    @PutMapping("/{restockOrderSpecificId}")
//    public ResponseEntity<RestockSpecificDto> updateRestockOrderSpecific(@PathVariable(name = "restockOrderSpecificId") Integer restockOrderSpecificId,
//                                                                         @RequestBody UpdateRestockSpecificDto updateRestockSpecificDto) {
//        RestockSpecificDto restockSpecific = service.updateRestockOrderSpecific(restockOrderSpecificId, updateRestockSpecificDto);
//        return ResponseEntity.ok(restockSpecific);
//    }

    // 재고 삭제
//    @Operation(summary = "발주리스트 내 재료 삭제", description = "발주리스트 내 재료 삭제 요청을 수행하는 API입니다.")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "발주리스트 내 재료 삭제 성공",
//                    content = @Content(examples = {
//                            @ExampleObject(
//                                    name = "RestockOrderSpecific 삭제 ",
//                                    summary = "RestockOrderSpecific 삭제 body의 예시",
//                                    value = " "
//                            )
//                    })
//            )
//    })
//    @DeleteMapping("/{restockOrderSpecificId}")
//    public ResponseEntity<Void> deleteRestockOrderSpecific(@PathVariable(name = "restockOrderSpecificId") Integer restockOrderSpecificId) {
//        service.deleteRestockOrderSpecific(restockOrderSpecificId);
//        return ResponseEntity.ok().build();
//    }

}