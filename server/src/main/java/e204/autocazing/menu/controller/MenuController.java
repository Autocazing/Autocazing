package e204.autocazing.menu.controller;

import e204.autocazing.db.entity.IngredientEntity;
import e204.autocazing.db.entity.MenuEntity;
import e204.autocazing.db.entity.MenuIngredientEntity;
import e204.autocazing.menu.dto.*;
import e204.autocazing.menu.service.MenuService;
import lombok.Getter;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/menus")
public class MenuController {

    @Autowired
    private MenuService menuService;

    // 메뉴 등록
    @PostMapping
    public ResponseEntity createMenu(@RequestBody PostMenuDto postMenuDto) {
        menuService.createMenu(postMenuDto);
        return new ResponseEntity(HttpStatus.CREATED);

    }

    @PatchMapping("/{menuId}")
    public ResponseEntity updateMenu(@PathVariable(name = "menuId") Integer menuId,@RequestBody UpdateMenuDto updateMenuDto){
        MenuDto menu = menuService.updateMenu(updateMenuDto,menuId);
        System.out.println("updateMenu  실행 후");
        return ResponseEntity.ok(menu);

    }

    @DeleteMapping("/{menuId}")
    public ResponseEntity deleteMenu(@PathVariable(name = "menuId") Integer menuId){
        menuService.deleteMenu(menuId);
        return ResponseEntity.ok().build();

    }

    @GetMapping("/{menuId}")
    public ResponseEntity getMenuById(@PathVariable(name = "menuId") Integer menuId){
        MenuDto menu = menuService.findMenuById(menuId);
        return ResponseEntity.ok(menu);
    }

    @GetMapping("")
    public ResponseEntity getAllmenus(){
        List<MenuDto> menus = menuService.findAllMenus();
        return ResponseEntity.ok(menus);
    }
}
