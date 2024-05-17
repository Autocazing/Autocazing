package e204.autocazing.restockSpecific.service;

import e204.autocazing.db.entity.IngredientEntity;
import e204.autocazing.db.entity.RestockOrderEntity;
import e204.autocazing.db.entity.RestockOrderSpecificEntity;
import e204.autocazing.db.repository.IngredientRepository;
import e204.autocazing.db.repository.RestockOrderRepository;
import e204.autocazing.db.repository.RestockOrderSpecificRepository;
import e204.autocazing.restock.dto.UpdatedRestockSpecificDto;
import e204.autocazing.restockSpecific.dto.PostRestockSpecificDto;
import e204.autocazing.restockSpecific.dto.RestockSpecificResponseDto;
import e204.autocazing.restockSpecific.dto.UpdateRestockSpecificDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
//        RestockOrderEntity restockOrder = restockOrderRepository.findById(postRestockSpecificDto.getRestockOrderId())
//                .orElseThrow(() -> new RuntimeException("RestockOrder not found with id: " + postRestockSpecificDto.getRestockOrderId()));
        int totalPrice = ingredient.getIngredientPrice() * postRestockSpecificDto.getIngredientQuantity();

        restockSpecific.setIngredientName(ingredient.getIngredientName());
//        restockSpecific.setRestockOrder(restockOrder);
        //계산 된 값. 재료 값 * 주문 수량
        restockSpecific.setIngredientPrice(totalPrice);
        restockSpecific.setIngredientQuantity(postRestockSpecificDto.getIngredientQuantity());

        restockOrderSpecificRepository.save(restockSpecific);

    }

    // 전체 조회
    public List<RestockSpecificResponseDto> findAllRestockOrderSpecifics() {
        return restockOrderSpecificRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 상세 조회
    public RestockSpecificResponseDto findRestockOrderSpecificById(Integer restockOrderSpecificId) {
        RestockOrderSpecificEntity restockSpecific = restockOrderSpecificRepository.findById(restockOrderSpecificId)
                .orElseThrow(() -> new RuntimeException("RestockOrderSpecific not found"));
        return convertToDto(restockSpecific);
    }

    private RestockSpecificResponseDto convertToDto(RestockOrderSpecificEntity restockSpecific) {
        return RestockSpecificResponseDto.builder()
                .restockOrderSpecificId(restockSpecific.getRestockOrderSpecificId())
                .restockOrderId(restockSpecific.getRestockOrder().getRestockOrderId())
                .ingredientQuantity(restockSpecific.getIngredientQuantity())
                .ingredientPrice(restockSpecific.getIngredientPrice())
                .status(String.valueOf(restockSpecific.getRestockOrder().getStatus()))
                .build();
    }

    // Update
    @Transactional
    public RestockSpecificResponseDto updateRestockOrderSpecific(Integer restockOrderSpecificId, UpdateRestockSpecificDto updatedRestockOrderSpecific) {

        RestockOrderSpecificEntity restockOrderSpecificEntity = restockOrderSpecificRepository.findById(restockOrderSpecificId)
                .orElseThrow(() -> new EntityNotFoundException("Specific not found with given IDs"));



        IngredientEntity ingredientEntity =ingredientRepository.findById(restockOrderSpecificEntity.getIngredientId())
                .orElseThrow(() -> new EntityNotFoundException("Ingredient not found with ingredientId" + restockOrderSpecificEntity.getIngredientId()));

        // 업데이트 로직
        restockOrderSpecificEntity.setIngredientQuantity(updatedRestockOrderSpecific.getIngredientQuantity());
        restockOrderSpecificEntity.setIngredientPrice(ingredientEntity.getIngredientPrice() * updatedRestockOrderSpecific.getIngredientQuantity());

        restockOrderSpecificRepository.save(restockOrderSpecificEntity);
        return convertToDto(restockOrderSpecificEntity);
    }

     //Delete
    @Transactional
    public void deleteRestockOrderSpecific(Integer restockOrderSpecificId) {
        restockOrderSpecificRepository.deleteById(restockOrderSpecificId);
    }

    @Transactional
    public List<UpdatedRestockSpecificDto> updateRestockOrderSpecificStatus(Integer restockOrderId, Integer venderId, RestockOrderSpecificEntity.RestockSpecificStatus status) {
        // restockOrderId로 발주 상세 리스트
        List<RestockOrderSpecificEntity> restockOrderSpecificEntityList = restockOrderSpecificRepository.findByRestockOrderRestockOrderId(restockOrderId);
        List<UpdatedRestockSpecificDto> updatedRestockSpecificDtoList = new ArrayList<>();

        // 발주 상세 리스트의 재료 ID
        for(RestockOrderSpecificEntity restockOrderSpecificEntity : restockOrderSpecificEntityList) {
            Integer ingredientId = restockOrderSpecificEntity.getIngredientId();
            Integer specificVenderId = ingredientRepository.findByIngredientId(ingredientId);

            if (Objects.equals(venderId, specificVenderId)) {
                restockOrderSpecificEntity.setStatus(status);
                restockOrderSpecificRepository.save(restockOrderSpecificEntity); // 상태 변경 후 저장
                UpdatedRestockSpecificDto dto = convertToUpdatedDto(restockOrderSpecificEntity);
                updatedRestockSpecificDtoList.add(dto);
            }
        }
        return updatedRestockSpecificDtoList;
    }

    private UpdatedRestockSpecificDto convertToUpdatedDto(RestockOrderSpecificEntity entity) {
        return new UpdatedRestockSpecificDto(
            entity.getRestockOrderSpecificId(),
            entity.getIngredientQuantity(),
            entity.getIngredientPrice(),
            entity.getIngredientName(),
            entity.getIngredientId(),
            entity.getStatus()
        );
    }
}
