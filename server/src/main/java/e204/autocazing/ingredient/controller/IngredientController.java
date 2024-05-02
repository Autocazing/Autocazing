//package e204.autocazing.ingredient.controller;
//
//import e204.autocazing.ingredient.service.IngredientService;
//import e204.autocazing.db.entity.IngredientEntity;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/ingredients")
//public class IngredientController {
//    @Autowired
//    private IngredientService ingredientService;
//
//    // 재료 등록
//    @PostMapping
//    public ResponseEntity<IngredientEntity> createIngredient(@RequestBody IngredientEntity ingredient) {
//        IngredientEntity savedIngredient = ingredientService.createIngredient(ingredient);
//        return new ResponseEntity(HttpStatus.CREATED);
//    }
//
//    // 재료 수정
//    @PatchMapping("/{ingredientId}")
//    public ResponseEntity<IngredientEntity> updateIngredient(@PathVariable Long ingredientId, @RequestBody IngredientEntity ingredientDetails) {
//        IngredientEntity updatedIngredient = ingredientService.updateIngredient(ingredientId, ingredientDetails);
//        return ResponseEntity.ok(updatedIngredient);
//    }
//
//    // 재료 삭제
//    @DeleteMapping("/{ingredientId}")
//    public ResponseEntity<Void> deleteIngredient(@PathVariable Long ingredientId) {
//        ingredientService.deleteIngredient(ingredientId);
//        return ResponseEntity.ok().build();
//    }
//
//    // 전체 재료 조회
//    @GetMapping
//    public ResponseEntity<List<IngredientEntity>> getAllIngredients() {
//        List<IngredientEntity> ingredients = ingredientService.findAllIngredients();
//        return ResponseEntity.ok(ingredients);
//    }
//
//    // 재료 상세 조회
//    @GetMapping("/{ingredientId}")
//    public ResponseEntity<IngredientEntity> getIngredientById(@PathVariable Long ingredientId) {
//        IngredientEntity ingredient = ingredientService.findIngredientById(ingredientId);
//        return ResponseEntity.ok(ingredient);
//    }
//}
