package e204.autocazing.menu.controller;

import e204.autocazing.db.entity.IngredientEntity;
import e204.autocazing.db.entity.MenuEntity;
import e204.autocazing.db.entity.MenuIngredientEntity;
import e204.autocazing.menu.dto.MenuIngredientDto;
import e204.autocazing.menu.dto.PostMenuDto;
import e204.autocazing.menu.service.MenuService;
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


}
