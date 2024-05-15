package e204.autocazing.menu.service;

import e204.autocazing.db.entity.IngredientEntity;
import e204.autocazing.db.entity.MenuEntity;
import e204.autocazing.db.entity.MenuIngredientEntity;
import e204.autocazing.db.entity.StoreEntity;
import e204.autocazing.db.repository.IngredientRepository;
import e204.autocazing.db.repository.MenuIngredientRepository;
import e204.autocazing.db.repository.MenuRepository;
import e204.autocazing.db.repository.StoreRepository;
import e204.autocazing.exception.ResourceNotFoundException;
import e204.autocazing.menu.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    public void createMenu(PostMenuDto postMenuDto,String loginId) {
        MenuEntity menu = new MenuEntity();
        menu.setMenuName(postMenuDto.getMenuName());
        menu.setMenuPrice(postMenuDto.getMenuPrice());
        menu.setOnEvent(postMenuDto.getOnEvent());
        menu.setDiscountRate(postMenuDto.getDiscountRate());
        menu.setImageUrl(postMenuDto.getImageUrl());
        menu.setSoldOut(postMenuDto.getSoldOut());
        // 가게 정보 설정
        StoreEntity storeEntity = storeRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with loginId: " + loginId));
        menu.setStore(storeEntity);
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

    public List<MenuDetailsDto> findAllMenus(String loginId) {
        StoreEntity storeEntity = storeRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with loginId: " + loginId));
        List<MenuEntity> menuEntities = menuRepository.findByStore(storeEntity);
        return menuEntities.stream()
                .map(this::convertToMenuDetailsDto)
                .collect(Collectors.toList());
    }
    //List<MenuDetailsDto> 채우기
    private MenuDetailsDto convertToMenuDetailsDto(MenuEntity menuEntity) {
        MenuDetailsDto menuDetailsDto = new MenuDetailsDto();
        menuDetailsDto.setMenuId(menuEntity.getMenuId());
        menuDetailsDto.setMenuName(menuEntity.getMenuName());
        menuDetailsDto.setMenuPrice(menuEntity.getMenuPrice());
        menuDetailsDto.setOnEvent(menuEntity.getOnEvent());
        menuDetailsDto.setSoldOut(menuEntity.getSoldOut());
        menuDetailsDto.setDiscountRate(menuEntity.getDiscountRate());
        menuDetailsDto.setImageUrl(menuEntity.getImageUrl());
        menuDetailsDto.setStoreId(menuEntity.getStore() != null ? menuEntity.getStore().getStoreId() : null);
        menuDetailsDto.setIngredientoDtoList(menuEntity.getMenuIngredients().stream()
                .map(this::convertToIngredientoDto)
                .collect(Collectors.toList()));
        return menuDetailsDto;
    }
    //전체 조회 IngredientDtoList 채우기
    private IngredientoDto convertToIngredientoDto(MenuIngredientEntity menuIngredient) {
        IngredientoDto ingredientoDto = new IngredientoDto();
        ingredientoDto.setIngredientId(menuIngredient.getIngredient().getIngredientId());
        ingredientoDto.setIngredientName(menuIngredient.getIngredient().getIngredientName());
        ingredientoDto.setCapacity(menuIngredient.getCapacity()); // 메뉴에 들어가는 재료의 양
        return ingredientoDto;
    }

    public List<Map<String, Object>> getMenuSales(String type, Integer menuId, String loginId) {
        Integer storeId = storeRepository.findStoreIdByLoginId(loginId);

        List<Map<String, Object>> saleDtoList = new ArrayList<>();
        LocalDateTime currentTime = LocalDateTime.now().plusHours(9);

        if(type.equals("day")){
            LocalDateTime startTime = currentTime.minusDays(30);
            saleDtoList = menuRepository.calculateDailySales(startTime, menuId, storeId);

            fillMissingDays(saleDtoList, startTime.toLocalDate(), LocalDate.from(LocalDateTime.now().plusHours(9)));
            Collections.sort(saleDtoList, new Comparator<Map<String, Object>>() {
                @Override
                public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                    LocalDate date1 = ((Date) o1.get("date")).toLocalDate();
                    LocalDate date2 = ((Date) o2.get("date")).toLocalDate();
                    return date1.compareTo(date2);
                }
            });
        }
        else if(type.equals("week")){
            LocalDateTime startTime = currentTime.minusWeeks(12);
            saleDtoList = menuRepository.calculateWeekSales(startTime, menuId, storeId);

            fillMissingWeeks(saleDtoList, currentTime);

            Collections.sort(saleDtoList, new Comparator<Map<String, Object>>() {
                @Override
                public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                    Integer year1 = (Integer) o1.get("year");
                    Integer week1 = (Integer) o1.get("week");
                    Integer year2 = (Integer) o2.get("year");
                    Integer week2 = (Integer) o2.get("week");
                    int yearCompare = year1.compareTo(year2);
                    if (yearCompare == 0) {
                        return week1.compareTo(week2);
                    }
                    return yearCompare;
                }
            });
        }
        else if(type.equals("month")){
            LocalDateTime startTime = currentTime.minusMonths(12);
            saleDtoList = menuRepository.calculateMonthSales(startTime, menuId, storeId);

            fillMissingMonths(saleDtoList, currentTime);

            Collections.sort(saleDtoList, new Comparator<Map<String, Object>>() {
                @Override
                public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                    Integer year1 = (Integer) o1.get("year");
                    Integer month1 = (Integer) o1.get("month");
                    Integer year2 = (Integer) o2.get("year");
                    Integer month2 = (Integer) o2.get("month");
                    int yearCompare = year1.compareTo(year2);
                    if (yearCompare == 0) {
                        return month1.compareTo(month2);
                    }
                    return yearCompare;
                }
            });
        }
        return saleDtoList;
    }

    private void fillMissingDays(List<Map<String, Object>> sales, LocalDate start, LocalDate end) {
        Set<LocalDate> existingDates = sales.stream()
            .map(s -> ((Date) s.get("date")).toLocalDate())
            .collect(Collectors.toSet());
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            if (!existingDates.contains(date)) {
                Map<String, Object> missingDay = new HashMap<>();
                missingDay.put("date", Date.valueOf(date));
                missingDay.put("totalSales", 0);
                sales.add(missingDay);
            }
        }
    }

    private void fillMissingWeeks(List<Map<String, Object>> sales, LocalDateTime currentTime) {
        Set<String> existingWeeks = sales.stream()
            .map(s -> s.get("year") + "-" + s.get("week"))
            .collect(Collectors.toSet());

        int currentYear = currentTime.getYear();
        int currentWeek = currentTime.get(WeekFields.ISO.weekOfWeekBasedYear());

        for (int i = 0; i < 12; i++) {
            if (!existingWeeks.contains(currentYear + "-" + currentWeek)) {
                Map<String, Object> missingWeek = new HashMap<>();
                missingWeek.put("week", currentWeek);
                missingWeek.put("year", currentYear);
                missingWeek.put("totalSales", 0);
                sales.add(missingWeek);
            }
            currentTime = currentTime.minusWeeks(1);
            currentWeek = currentTime.get(WeekFields.ISO.weekOfWeekBasedYear());
            currentYear = currentTime.getYear();
        }
    }


    private void fillMissingMonths(List<Map<String, Object>> sales, LocalDateTime currentTime) {
        Set<String> existingMonths = sales.stream()
            .map(s -> s.get("year") + "-" + s.get("month"))
            .collect(Collectors.toSet());

        int currentYear = currentTime.getYear();
        int currentMonth = currentTime.getMonthValue();

        for (int i = 0; i < 12; i++) {
            if (!existingMonths.contains(currentYear + "-" + currentMonth)) {
                Map<String, Object> missingMonth = new HashMap<>();
                missingMonth.put("month", currentMonth);
                missingMonth.put("year", currentYear);
                missingMonth.put("totalSales", 0);
                sales.add(missingMonth);
            }
            currentTime = currentTime.minusMonths(1);
            currentMonth = currentTime.getMonthValue();
            currentYear = currentTime.getYear();
        }
    }
}