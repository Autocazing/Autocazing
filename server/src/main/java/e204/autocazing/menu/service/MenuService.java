package e204.autocazing.menu.service;

import e204.autocazing.db.entity.IngredientEntity;
import e204.autocazing.db.entity.MenuEntity;
import e204.autocazing.db.entity.MenuIngredientEntity;
import e204.autocazing.db.entity.StoreEntity;
import e204.autocazing.db.repository.IngredientRepository;
import e204.autocazing.db.repository.MenuIngredientRepository;
import e204.autocazing.db.repository.MenuRepository;
import e204.autocazing.db.repository.StoreRepository;
import e204.autocazing.menu.dto.MenuIngredientDto;
import e204.autocazing.menu.dto.PostMenuDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

    public void createMenu(PostMenuDto postMenuDto) {
        MenuEntity menu = new MenuEntity();
        menu.setMenuName(postMenuDto.getMenuName());
        menu.setMenuPrice(postMenuDto.getMenuPrice());
        menu.setOnEvent(postMenuDto.getOnEvent());
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

}
