package e204.autocazing.scale.controller;

import e204.autocazing.db.entity.IngredientScaleEntity;
import e204.autocazing.scale.dto.PatchIngredientScaleDto;
import e204.autocazing.scale.dto.PostIngredientScaleDto;
import e204.autocazing.scale.service.IngredientScaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredientScales")
public class IngredientScaleController {
    @Autowired
    private IngredientScaleService ingredientScaleService;

    // 단위 추가
    @PostMapping
    public ResponseEntity<IngredientScaleEntity> createIngredientScale(@RequestBody PostIngredientScaleDto postScaleDto) {
        IngredientScaleEntity savedScale = ingredientScaleService.createIngredientScale(postScaleDto);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    // 단위 수정
    @PatchMapping("/{scaleId}")
    public ResponseEntity<IngredientScaleEntity> updateIngredientScale(@PathVariable(name = "scaleId") Integer scaleId, @RequestBody PatchIngredientScaleDto patchIngredientScaleDto) {
        IngredientScaleEntity updatedScale = ingredientScaleService.updateIngredientScale(scaleId, patchIngredientScaleDto);
        return ResponseEntity.ok(updatedScale);
    }

    // 단위 삭제
    @DeleteMapping("/{scaleId}")
    public ResponseEntity<Void> deleteIngredientScale(@PathVariable(name = "scaleId") Integer scaleId) {
        ingredientScaleService.deleteIngredientScale(scaleId);
        return ResponseEntity.ok().build();
    }

    // 단위 목록 조회
    @GetMapping
    public ResponseEntity<List<IngredientScaleEntity>> getAllIngredientScales() {
        List<IngredientScaleEntity> scales = ingredientScaleService.findAllIngredientScales();
        return ResponseEntity.ok(scales);
    }
    @GetMapping("/{scaleId}")
    public ResponseEntity<IngredientScaleEntity> getIngredientScaleById(@PathVariable(name = "scaleId") Integer scaleId) {
        IngredientScaleEntity scale = ingredientScaleService.findIngredientScaleById(scaleId);
        return ResponseEntity.ok(scale);
    }
}
