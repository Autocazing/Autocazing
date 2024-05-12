package e204.autocazing.ingredient.controller;

import e204.autocazing.ingredient.dto.IngredientDetails;
import e204.autocazing.ingredient.dto.PatchIngredientDto;
import e204.autocazing.ingredient.dto.PostIngredientDto;
import e204.autocazing.ingredient.dto.IngredientDto;
import e204.autocazing.ingredient.service.IngredientService;
import e204.autocazing.db.entity.IngredientEntity;
import e204.autocazing.scale.dto.IngredientScaleDto;
import e204.autocazing.stock.dto.StockDetailsDto;
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
@RequestMapping("/api/ingredients")
public class IngredientController {
    @Autowired
    private IngredientService ingredientService;

    // 재료 등록
    @Operation(summary = "재료 등록 요청", description = "재료를 등록 했을 때 동작을 수행하는 API입니다. " +
            "                                       단위 직접입력 => ScaleId=0 으로 주면 백에서 저장함.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "재료 등록 성공")
    })
    @PostMapping
    public ResponseEntity<IngredientEntity> createIngredient(@RequestBody PostIngredientDto postIngredientDto) {
        ingredientService.createIngredient(postIngredientDto);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    // 재료 수정
    @Operation(summary = "재료 수정 요청", description = "재료 수정을 수행하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "재료 수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IngredientDetails.class)
                    )
            )
    })
    @PutMapping("/{ingredientId}")
    public ResponseEntity<IngredientDetails> updateIngredient(@Parameter(in = ParameterIn.PATH) @PathVariable(name = "ingredientId") Integer ingredientId, @RequestBody PatchIngredientDto ingredientDto) {
        IngredientDetails updateIngredient = ingredientService.updateIngredient(ingredientId, ingredientDto);
        return ResponseEntity.ok(updateIngredient);
    }

    // 재료 삭제
    @Operation(summary = "재료 삭제 요청", description = "재료 삭제를 수행하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "재료 삭제 성공")
    })
    @DeleteMapping("/{ingredientId}")
    public ResponseEntity<Void> deleteIngredient(@Parameter(in = ParameterIn.PATH) @PathVariable(name = "ingredientId") Integer ingredientId) {
        ingredientService.deleteIngredient(ingredientId);
        return ResponseEntity.ok().build();
    }

    // 전체 재료 조회
    @Operation(summary = "재료 목록 조회 요청", description = "재료 목록 조회를 수행하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "재료 목록 조회 성공",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = IngredientDetails.class)), examples = {
//                @ExampleObject(
//                    name = "재료 목록 조회 body",
//                    summary = "재료 목록 조회 body의 예시",
//                    value = "[{\"ingredientId\": 1,\n"
//                        + "    \"venderName\": 동민상사,\n"
//                        + "    \"ingredientName\": \"milk\",\n"
//                        + "    \"ingredientPrice\": 5000,\n"
//                        + "    \"ingredientCapacity\": 5,\n"
//                        + "    \"scaleId\": 1,\n"
//                        + "    \"minimumCount\": 15,\n"
//                        + "    \"deliveryTime\": 1,\n"
//                        + "    \"orderCount\": 10}]"
//                )
            })
        )
    })
    @GetMapping
    public ResponseEntity<List<IngredientDetails>> getAllIngredients() {
        List<IngredientDetails> ingredients = ingredientService.findAllIngredients();
        return ResponseEntity.ok(ingredients);
    }

    // 재료 상세 조회 (쓸 일이 있나?)
    @Operation(summary = "재료 조회 요청", description = "재료 조회를 수행하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "재료 조회 성공",
            content = @Content(examples = {
                @ExampleObject(
                    name = "재료 조회 body",
                    summary = "재료 조회 body의 예시",
                    value = "{\"ingredientId\": 1,\n"
                        + "    \"storeId\": 1,\n"
                        + "    \"venderId\": 1,\n"
                        + "    \"ingredientName\": \"milk\",\n"
                        + "    \"ingredientPrice\": 5000,\n"
                        + "    \"ingredientCapacity\": 5,\n"
                        + "    \"scaleId\": 1,\n"
                        + "    \"minimumCount\": 15,\n"
                        + "    \"deliveryTime\": 1,\n"
                        + "    \"orderCount\": 10}"
                )
            })
        )
    })
    @GetMapping("/{ingredientId}")
    public ResponseEntity<IngredientDetails> getIngredientById(@Parameter(in = ParameterIn.PATH) @PathVariable(name = "ingredientId") Integer ingredientId) {
        IngredientDetails ingredient = ingredientService.findIngredientById(ingredientId);
        return ResponseEntity.ok(ingredient);
    }
}