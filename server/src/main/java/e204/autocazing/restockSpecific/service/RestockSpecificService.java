package e204.autocazing.restockSpecific.service;

import e204.autocazing.db.entity.IngredientEntity;
import e204.autocazing.db.entity.RestockOrderEntity;
import e204.autocazing.db.entity.RestockOrderSpecificEntity;
import e204.autocazing.db.repository.IngredientRepository;
import e204.autocazing.db.repository.RestockOrderRepository;
import e204.autocazing.db.repository.RestockOrderSpecificRepository;
import e204.autocazing.ingredient.service.IngredientService;
import e204.autocazing.restock.service.RestockOrderService;
import e204.autocazing.restockSpecific.dto.PostRestockSpecificDto;
import e204.autocazing.restockSpecific.dto.RestockSpecificDto;
import e204.autocazing.restockSpecific.dto.UpdateRestockSpecificDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestockSpecificService {
    @Autowired
    private RestockOrderSpecificRepository restockOrderSpecificRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private RestockOrderRepository restockOrderRepository;

    @Transactional
    public void createRestockOrderSpecific(PostRestockSpecificDto postRestockSpecificDto) {
        RestockOrderSpecificEntity restockSpecific = new RestockOrderSpecificEntity();

        IngredientEntity ingredient = ingredientRepository.findById(postRestockSpecificDto.getIngredientId())
                .orElseThrow(() -> new RuntimeException("Ingredient not found with id: " + postRestockSpecificDto.getIngredientId()));
        RestockOrderEntity restockOrder = restockOrderRepository.findById(postRestockSpecificDto.getRestockOrderId())
                .orElseThrow(() -> new RuntimeException("RestockOrder not found with id: " + postRestockSpecificDto.getRestockOrderId()));
        int totalPrice = ingredient.getIngredientPrice() * postRestockSpecificDto.getIngredientQuantity();
        restockSpecific.setIngredient(ingredient);
        restockSpecific.setRestockOrder(restockOrder);
        //계산 된 값. 재료 값 * 주문 수량
        restockSpecific.setIngredientPrice(totalPrice);
        restockSpecific.setIngredientQuantity(postRestockSpecificDto.getIngredientQuantity());

        restockOrderSpecificRepository.save(restockSpecific);

    }

    // 전체 조회
    public List<RestockSpecificDto> findAllRestockOrderSpecifics() {
        return restockOrderSpecificRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 상세 조회
    public RestockSpecificDto findRestockOrderSpecificById(Integer restockOrderSpecificId) {
        RestockOrderSpecificEntity restockSpecific = restockOrderSpecificRepository.findById(restockOrderSpecificId)
                .orElseThrow(() -> new RuntimeException("RestockOrderSpecific not found"));
        return convertToDto(restockSpecific);
    }

    private RestockSpecificDto convertToDto(RestockOrderSpecificEntity restockSpecific) {
        return new RestockSpecificDto(
                restockSpecific.getRestockOrderSpecificId(),
                restockSpecific.getIngredient().getIngredientId(),
                restockSpecific.getRestockOrder().getRestockOrderId(),
                restockSpecific.getIngredientQuantity(),
                restockSpecific.getIngredientPrice()
        );
    }

    // Update
    @Transactional
    public RestockSpecificDto updateRestockOrderSpecific(Integer restockOrderSpecificId, UpdateRestockSpecificDto updatedRestockOrderSpecific) {
        RestockOrderSpecificEntity restockSpecific = restockOrderSpecificRepository.findById(restockOrderSpecificId)
                .orElseThrow(() -> new RuntimeException("RestockOrderSpecific not found"));

        // 수량 변경 및 가격 재계산
        restockSpecific.setIngredientQuantity(updatedRestockOrderSpecific.getIngredientQuantity());
        Integer newPrice = restockSpecific.getIngredient().getIngredientPrice() * updatedRestockOrderSpecific.getIngredientQuantity();
        restockSpecific.setIngredientPrice(newPrice);
        restockOrderSpecificRepository.save(restockSpecific);
        RestockSpecificDto updatedRestockSpecific = new RestockSpecificDto();

        updatedRestockSpecific.setRestockOrderSpecificId(restockOrderSpecificId);
        updatedRestockSpecific.setIngredientId(restockSpecific.getIngredient().getIngredientId());
        updatedRestockSpecific.setRestockOrderId(restockSpecific.getRestockOrder().getRestockOrderId());
        updatedRestockSpecific.setIngredientPrice(restockSpecific.getIngredientPrice());
        updatedRestockSpecific.setIngredientQuantity(restockSpecific.getIngredientQuantity());

        return updatedRestockSpecific;
    }

    // Delete
    @Transactional
    public void deleteRestockOrderSpecific(Integer restockOrderSpecificId) {
        restockOrderSpecificRepository.deleteById(restockOrderSpecificId);
    }
}
