package e204.autocazing.menu.controller;

import e204.autocazing.menu.dto.*;
import e204.autocazing.menu.service.MenuService;
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
import java.util.Map;

@RestController
@RequestMapping("api/menus")
public class MenuController {

    @Autowired
    private MenuService menuService;

    // 메뉴 등록
    @Operation(summary = "메뉴 등록 요청", description = "메뉴를 등록 했을 때 동작을 수행하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "메뉴 등록 성공")
    })
    @PostMapping
    public ResponseEntity createMenu(@RequestBody PostMenuDto postMenuDto) {
        menuService.createMenu(postMenuDto);
        return new ResponseEntity(HttpStatus.CREATED);

    }

    @Operation(summary = "메뉴 삭제 요청", description = "메뉴 삭제를 수행하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "메뉴 삭제 성공")
    })
    @DeleteMapping("/{menuId}")
    public ResponseEntity deleteMenu(@Parameter(in = ParameterIn.PATH) @PathVariable(name = "menuId") Integer menuId){
        menuService.deleteMenu(menuId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "메뉴 수정 요청", description = "메뉴 수정을 수행하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "메뉴 수정 성공",
            content = @Content(examples = {
                @ExampleObject(
                    name = "메뉴 수정 반환 body",
                    summary = "메뉴 수정 반환 body의 예시",
                    value = "{\"menuId\": 1, \n"
                        + "\"menuName\": \"latte\",\n"
                        + "  \"menuPrice\": 5500,\n"
                        + "  \"onEvent\": false,\n"
                        + "  \"ingredients\": [\n"
                        + "    {\n"
                        + "      \"ingredientId\": 1,\n"
                        + "      \"capacity\": 50\n"
                        + "    }\n"
                        + "  ],\n"
                        + "  \"storeId\": 1}"
                )
            })
        )
    })
    @PatchMapping("/{menuId}")
    public ResponseEntity updateMenu(@Parameter(in = ParameterIn.PATH) @PathVariable(name = "menuId") Integer menuId
        ,@RequestBody UpdateMenuDto updateMenuDto){
        MenuDto menu = menuService.updateMenu(updateMenuDto,menuId);
        return ResponseEntity.ok(menu);
    }

    @Operation(summary = "메뉴 조회 요청", description = "메뉴 조회를 수행하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "메뉴 조회 성공",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = MenuDto.class)),
                examples = {
                @ExampleObject(
                    name = "메뉴 조회 body",
                    summary = "메뉴 조회 body의 예시",
                    value = "{\"menuId\": 1, \n"
                        + "\"menuName\": \"latte\",\n"
                        + "  \"menuPrice\": 5500,\n"
                        + "  \"onEvent\": false,\n"
                        + "  \"discountRate\": 30,\n"
                        + "  \"ingredients\": [\n"
                        + "    {\n"
                        + "      \"ingredientId\": 1,\n"
                        + "      \"capacity\": 50\n"
                        + "    }\n"
                        + "  ],\n"
                        + "  \"storeId\": 1}"
                )
            })
        )
    })
    @GetMapping("/{menuId}")
    public ResponseEntity getMenuById(@Parameter(in = ParameterIn.PATH) @PathVariable(name = "menuId") Integer menuId){
        MenuDto menu = menuService.findMenuById(menuId);
        return ResponseEntity.ok(menu);
    }

    @Operation(summary = "메뉴 목록 조회 요청", description = "메뉴 목록 조회를 수행하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "메뉴 목록 조회 성공",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = MenuDto.class)), examples = {
                @ExampleObject(
                    name = "메뉴 목록 조회 body",
                    summary = "메뉴 목록 조회 body의 예시",
                    value = "[{\"menuId\": 1, \n"
                        + "\"menuName\": \"latte\",\n"
                        + "  \"menuPrice\": 5500,\n"
                        + "  \"onEvent\": false,\n"
                        + "  \"discountRate\": 30,\n"
                        + "  \"ingredients\": [\n"
                        + "    {\n"
                        + "      \"ingredientId\": 1,\n"
                        + "      \"capacity\": 50\n"
                        + "    }\n"
                        + "  ],\n"
                        + "  \"storeId\": 1}]"
                )
            })
        )
    })
    @GetMapping("")
    public ResponseEntity getAllmenus(){
        List<MenuDto> menus = menuService.findAllMenus();
        return ResponseEntity.ok(menus);
    }

    @GetMapping("/sales")
    public ResponseEntity getMenuSales(
        @Parameter(description = " 'day' 또는 'week' 또는 'month' 으로 요청할 수 있습니다.",
            required = true,
            schema = @Schema(type = "string", allowableValues = {"day", "week", "month"}))
        @RequestParam("type") String type, @RequestBody MenuSalesDto menuSalesDto){ //type : 일별 day, 주별 week, 월별 month
        List<Map<String, Object>> sales = menuService.getMenuSales(type, menuSalesDto);
        return ResponseEntity.ok(sales);
    }
}