package e204.autocazing.menu.service;

import e204.autocazing.db.entity.IngredientEntity;
import e204.autocazing.db.entity.MenuEntity;
import e204.autocazing.db.entity.MenuIngredientEntity;
import e204.autocazing.db.entity.StoreEntity;
import e204.autocazing.db.repository.IngredientRepository;
import e204.autocazing.db.repository.MenuIngredientRepository;
import e204.autocazing.db.repository.MenuRepository;
import e204.autocazing.db.repository.StoreRepository;
import e204.autocazing.menu.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private MenuIngredientRepository menuIngredientRepository;
    @Autowired
    private StoreRepository storeRepository;

    @Transactional
    public void createMenu(PostMenuDto postMenuDto) {
        MenuEntity menu = new MenuEntity();
        menu.setMenuPrice(postMenuDto.getMenuPrice());
        menu.setOnEvent(postMenuDto.getOnEvent());
        menu.setDiscountRate(postMenuDto.getDiscountRate());
        menu.setImageUrl(postMenuDto.getImageUrl());
        // 가게 정보 설정
        StoreEntity store = storeRepository.findById(postMenuDto.getStoreId())
                .orElseThrow(() -> new RuntimeException("Store not found with id: " + postMenuDto.getStoreId()));
        menu.setStore(store);
        // 저장 후 ID를 얻기 위해 먼저 메뉴를 저장
        menu = menuRepository.save(menu);
        // 메뉴와 재료의 관계 설정
        if (postMenuDto.getIngredients() != null) {
            for (MenuIngredientDto ingredientDto : postMenuDto.getIngredients()) {
                IngredientEntity ingredient = ingredientRepository.findById(ingredientDto.getIngredientId())
                        .orElseThrow(() -> new RuntimeException("Ingredient not found with id: " + ingredientDto.getIngredientId()));

                MenuIngredientEntity menuIngredient = new MenuIngredientEntity();
                menuIngredient.setMenu(menu);
                menuIngredient.setIngredient(ingredient);
                menuIngredient.setCapacity(ingredientDto.getCapacity());
                menuIngredientRepository.save(menuIngredient);
            }
        }
    }


    public MenuDto updateMenu(UpdateMenuDto updateMenuDto, Integer menuId) {
        MenuEntity menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("Menu not found with id " + menuId));
        if (updateMenuDto.getMenuName() != null) {
            menu.setMenuName(updateMenuDto.getMenuName());
        }
        if (updateMenuDto.getMenuPrice() != null) {
            menu.setMenuPrice(updateMenuDto.getMenuPrice());
        }
        if (updateMenuDto.getOnEvent() != null) {
            menu.setOnEvent(updateMenuDto.getOnEvent());
        }
        if(updateMenuDto.getDiscountRate() != 0 ){
            menu.setDiscountRate(updateMenuDto.getDiscountRate());
        }
        if(updateMenuDto.getImageUrl() != null){
            menu.setImageUrl(updateMenuDto.getImageUrl());
        }

        if(updateMenuDto.getIngredients() != null){
            //재료 수정을 어떻게 한담
            changeMenuIngredients(menu, updateMenuDto.getIngredients());
        }
//       menu.setUpdatedAt(LocalDateTime.now());
        menuRepository.save(menu);

        return convertToMenuDto(menu);
    }
    private void changeMenuIngredients(MenuEntity menu, List<MenuIngredientDto> ingredientDtos) {


        for (MenuIngredientDto dto : ingredientDtos) {
            IngredientEntity ingredient = ingredientRepository.findById(dto.getIngredientId())
                    .orElseThrow(() -> new RuntimeException("Ingredient not found with id: " + dto.getIngredientId()));

            MenuIngredientEntity menuIngredient = new MenuIngredientEntity();
            menuIngredient.setMenu(menu);
            menuIngredient.setIngredient(ingredient);
            menuIngredient.setCapacity(dto.getCapacity());
            menuIngredientRepository.save(menuIngredient);
        }
    }




    public void deleteMenu(Integer menuId) {
        menuRepository.deleteById(menuId);

    }

    public MenuDto findMenuById(Integer menuId) {
        MenuEntity menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("Menu not found with id " + menuId));
        MenuDto menuDto = new MenuDto();
        menuDto.setMenuId(menu.getMenuId());
        menuDto.setMenuName(menu.getMenuName());
        menuDto.setMenuPrice(menu.getMenuPrice());
        menuDto.setOnEvent(menu.getOnEvent());
        menuDto.setDiscountRate(menu.getDiscountRate());
        menuDto.setImageUrl(menu.getImageUrl());

        // MenuIngredientDto 리스트 변환
        List<MenuIngredientDto> ingredientDtos = menu.getMenuIngredients()
                .stream()
                .map(ingredient -> new MenuIngredientDto(
                        ingredient.getIngredient().getIngredientId(), // ID 매핑
                        ingredient.getCapacity()      // 수량을 capacity에 매핑
                ))
                .collect(Collectors.toList());
        menuDto.setIngredients(ingredientDtos);

        return menuDto;
    }

    public List<MenuDto> findAllMenus() {
        List<MenuEntity> menuEntities = menuRepository.findAll();
        return menuEntities.stream()
                .map(this::convertToMenuDto)
                .collect(Collectors.toList());
    }

    private MenuDto convertToMenuDto(MenuEntity menuEntity) {
        MenuDto menuDto = new MenuDto();
        menuDto.setMenuId(menuEntity.getMenuId());
        menuDto.setMenuName(menuEntity.getMenuName());
        menuDto.setMenuPrice(menuEntity.getMenuPrice());
        menuDto.setOnEvent(menuEntity.getOnEvent());
        menuDto.setDiscountRate(menuEntity.getDiscountRate());
        menuDto.setImageUrl(menuEntity.getImageUrl());
        menuDto.setStoreId(menuEntity.getStore() != null ? menuEntity.getStore().getStoreId() : null);

        List<MenuIngredientDto> ingredientDtos = menuEntity.getMenuIngredients()
                .stream()
                .map(ingredient -> new MenuIngredientDto(
                        ingredient.getIngredient().getIngredientId(), // 가정: IngredientEntity에 getId() 메서드 존재
                        ingredient.getCapacity()
                ))
                .collect(Collectors.toList());
        menuDto.setIngredients(ingredientDtos);

        return menuDto;
    }

    public List<Map<String, Object>> getMenuSales(String type, Integer menuId) {
        List<Map<String, Object>> saleDtoList = new ArrayList<>();

        //더미데이터 기준
        String dateTimeString = "2024-05-08 17:00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime baseDate = LocalDateTime.parse(dateTimeString, formatter);

        if(type.equals("day")){
            LocalDateTime currentTime = LocalDateTime.now().minusDays(30);
            saleDtoList = menuRepository.calculateDailySales(currentTime, menuId);
        }
        else if(type.equals("week")){
            LocalDateTime currentTime = LocalDateTime.now().minusWeeks(12);
            saleDtoList = menuRepository.calculateWeekSales(currentTime, menuId);
        }
        else if(type.equals("month")){
            LocalDateTime currentTime = LocalDateTime.now().minusMonths(12);
            saleDtoList = menuRepository.calculateMonthSales(currentTime, menuId);
        }

        return saleDtoList;
    }
}
