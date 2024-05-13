package e204.autocazing.scale.controller;

import e204.autocazing.db.entity.IngredientScaleEntity;
import e204.autocazing.sale.dto.SalesResponseDto;
import e204.autocazing.scale.dto.IngredientScaleDto;
import e204.autocazing.scale.dto.PatchIngredientScaleDto;
import e204.autocazing.scale.dto.PostIngredientScaleDto;
import e204.autocazing.scale.service.IngredientScaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
@RequestMapping("/api/ingredient-scales")
public class IngredientScaleController {

    @Autowired
    private IngredientScaleService ingredientScaleService;
    
    //단위 추가
    @Operation(summary = "단위 생성 요청", description = "직접 단위를 추가 했을 때 동작을 수행하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "단위 추가 성공")
    })
    @PostMapping
    public ResponseEntity<IngredientScaleEntity> createIngredientScale(@RequestBody PostIngredientScaleDto postScaleDto) {
        ingredientScaleService.createIngredientScale(postScaleDto);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    // 단위 수정
    @Operation(summary = "단위 수정 요청", description = "단위 수정 동작을 수행하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "단위 수정 성공",
            content = @Content(examples = {
                @ExampleObject(
                    name = "단위 생성 반환 body",
                    summary = "단위 생성 반환 body의 예시",
                    value = "{\"scaleId\": 1,\"unit\": \"ml\"}"
                )
            })
        )
    })
    @PutMapping("/{scaleId}")
    public ResponseEntity<IngredientScaleDto> updateIngredientScale(
        @Parameter(in = ParameterIn.PATH) @PathVariable(name = "scaleId") Integer scaleId,
        @RequestBody PatchIngredientScaleDto patchIngredientScaleDto) {

        IngredientScaleDto updatedScale = ingredientScaleService.updateIngredientScale(scaleId, patchIngredientScaleDto);
        return ResponseEntity.ok(updatedScale);
    }

    //단위 삭제
    @Operation(summary = "단위 삭제 요청", description = "단위 삭제를 수행하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "단위 삭제 성공")
    })
    @DeleteMapping("/{scaleId}")
    public ResponseEntity<Void> deleteIngredientScale(
        @Parameter(in = ParameterIn.PATH) @PathVariable(name = "scaleId") Integer scaleId) {
        ingredientScaleService.deleteIngredientScale(scaleId);
        return ResponseEntity.ok().build();
    }

    // 단위 목록 조회
    @Operation(summary = "단위 목록 조회 요청", description = "단위 목록 조회를 수행하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "단위 목록 조회 성공",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = IngredientScaleDto.class)), examples = {
                @ExampleObject(
                    name = "단위 목록 조회 body",
                    summary = "단위 목록 조회 body의 예시",
                    value = "[{\"scaleId\": 1,\"unit\": \"ml\"}]"
                )
            })
        )
    })
    @GetMapping
    public ResponseEntity<List<IngredientScaleDto>> getAllIngredientScales() {
        List<IngredientScaleDto> scales = ingredientScaleService.findAllIngredientScales();
        return ResponseEntity.ok(scales);
    }

    //단위 조회
    @Operation(summary = "단위 조회 요청", description = "단위 조회를 수행하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "단위 조회 성공",
            content = @Content(examples = {
                @ExampleObject(
                    name = "단위 조회 body",
                    summary = "단위 조회 body의 예시",
                    value = "{\"unit\": \"ml\"}"
                )
            })
        )
    })
    @GetMapping("/{scaleId}")
    public ResponseEntity<IngredientScaleDto> getIngredientScaleById(
        @Parameter(in = ParameterIn.PATH) @PathVariable(name = "scaleId") Integer scaleId) {
        IngredientScaleDto scale = ingredientScaleService.findIngredientScaleById(scaleId);
        return ResponseEntity.ok(scale);
    }
}
