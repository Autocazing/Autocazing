package e204.autocazing.ingredient.controller;

import e204.autocazing.ingredient.dto.PatchIngredientDto;
import e204.autocazing.ingredient.dto.PostIngredientDto;
import e204.autocazing.ingredient.dto.IngredientDto;
import e204.autocazing.ingredient.service.IngredientService;
import e204.autocazing.db.entity.IngredientEntity;
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
    @PostMapping
    public ResponseEntity<IngredientEntity> createIngredient(@RequestBody PostIngredientDto postIngredientDto) {
        ingredientService.createIngredient(postIngredientDto);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    // 재료 수정
    @PatchMapping("/{ingredientId}")
    public ResponseEntity<IngredientDto> updateIngredient(@PathVariable(name = "ingredientId") Integer ingredientId, @RequestBody PatchIngredientDto ingredientDto) {
        IngredientDto updateIngredient = ingredientService.updateIngredient(ingredientId, ingredientDto);
        return ResponseEntity.ok(updateIngredient);
    }

    // 재료 삭제
    @DeleteMapping("/{ingredientId}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable(name = "ingredientId") Integer ingredientId) {
        ingredientService.deleteIngredient(ingredientId);
        return ResponseEntity.ok().build();
    }

    // 전체 재료 조회
    @GetMapping
    public ResponseEntity<List<IngredientDto>> getAllIngredients() {
        List<IngredientDto> ingredients = ingredientService.findAllIngredients();
        return ResponseEntity.ok(ingredients);
    }

    // 재료 상세 조회
    @GetMapping("/{ingredientId}")
    public ResponseEntity<IngredientDto> getIngredientById(@PathVariable(name = "ingredientId") Integer ingredientId) {
        IngredientDto ingredient = ingredientService.findIngredientById(ingredientId);
        return ResponseEntity.ok(ingredient);
    }
}
