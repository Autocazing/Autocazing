//package e204.autocazing.menu.controller;
//
//import e204.autocazing.db.entity.IngredientEntity;
//import e204.autocazing.db.entity.MenuEntity;
//import e204.autocazing.db.entity.MenuIngredientEntity;
//import e204.autocazing.menu.dto.MenuIngredientDto;
//import e204.autocazing.menu.service.MenuService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@RestController
//@RequestMapping("api/menus")
//public class MenuController {
//
//    @Autowired
//    private MenuService menuService;
//
//    // 메뉴 등록
//    @PostMapping
//    public ResponseEntity<MenuEntity> createMenu(@RequestBody MenuCreationDTO menuCreationDTO) {
//        MenuEntity menu = convertToEntity(menuCreationDTO); // DTO를 엔티티로 변환하는 메서드
//        List<MenuIngredientEntity> menuIngredients = convertToMenuIngredientEntities(menuCreationDTO.getIngredients(), menu);
//        return ResponseEntity.ok(menuService.createMenu(menu, menuIngredients));
//    }
//
//    private MenuEntity convertToEntity(MenuCreationDTO menuCreationDTO) {
//        MenuEntity menu = new MenuEntity();
//        menu.setMenuName(menuCreationDTO.getMenuName());
//        menu.setMenuPrice(menuCreationDTO.getMenuPrice());
//        menu.setOnEvent(menuCreationDTO.getOnEvent());
//        return menu;
//    }
//
//    private List<MenuIngredientEntity> convertToMenuIngredientEntities(List<MenuIngredientDto> ingredientDTOs, MenuEntity menu) {
//        List<MenuIngredientEntity> menuIngredients = new ArrayList<>();
//        for (MenuIngredientDto dto : ingredientDTOs) {
//            MenuIngredientEntity entity = new MenuIngredientEntity();
//            entity.setMenu(menu);
//            IngredientEntity ingredient = ingredientRepository.findById(dto.getIngredientId()).orElseThrow();
//            entity.setIngredient(ingredient);
//            entity.setCapacity(dto.getQuantity());
//            menuIngredients.add(entity);
//        }
//        return menuIngredients;
//    }
//
//    // 메뉴 수정
//    @PutMapping("/{menuId}")
//    public ResponseEntity<MenuEntity> updateMenu(@PathVariable Long menuId, @RequestBody MenuEntity menuDetails, @RequestBody List<MenuIngredientEntity> menuIngredients) {
//        return ResponseEntity.ok(menuService.updateMenu(menuId, menuDetails, menuIngredients));
//    }
//
//    // 메뉴 삭제
//    @DeleteMapping("/{menuId}")
//    public ResponseEntity<Void> deleteMenu(@PathVariable Long menuId) {
//        menuService.deleteMenu(menuId);
//        return ResponseEntity.ok().build();
//    }
//
//    // 메뉴 조회
//    @GetMapping
//    public ResponseEntity<List<MenuEntity>> getAllMenus() {
//        return ResponseEntity.ok(menuService.findAllMenus());
//    }
//
//    // 메뉴 상세 조회
//    @GetMapping("/{menuId}")
//    public ResponseEntity<MenuEntity> getMenuById(@PathVariable Long menuId) {
//        return ResponseEntity.ok(menuService.findMenuById(menuId));
//    }
//}
