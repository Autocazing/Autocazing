package e204.autocazing.ingredient.service;

import e204.autocazing.db.entity.IngredientEntity;
import e204.autocazing.db.entity.IngredientScaleEntity;
import e204.autocazing.db.entity.StoreEntity;
import e204.autocazing.db.entity.VendorEntity;
import e204.autocazing.db.repository.IngredientRepository;
import e204.autocazing.db.repository.IngredientScaleRepository;
import e204.autocazing.db.repository.StoreRepository;
import e204.autocazing.db.repository.VendorRepository;
import e204.autocazing.ingredient.dto.IngredientDto;
import e204.autocazing.ingredient.dto.PatchIngredientDto;
import e204.autocazing.ingredient.dto.PostIngredientDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngredientService {
    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private VendorRepository vendorRepository;
    @Autowired
    private IngredientScaleRepository scaleRepository;
    public void createIngredient(PostIngredientDto postIngredientDto) {
        IngredientEntity ingredient = new IngredientEntity();
        ingredient.setIngredientName(postIngredientDto.getIngredientName());
        ingredient.setIngredientPrice(postIngredientDto.getIngredientPrice());
        ingredient.setIngredientCapacity(postIngredientDto.getIngredientCapacity());
        ingredient.setMinimumCount(postIngredientDto.getMinimumCount());
        ingredient.setDeliveryTime(postIngredientDto.getDeliveryTime());
        ingredient.setOrderCount(postIngredientDto.getOrderCount());

        // 참조 설정
        StoreEntity store = storeRepository.findById(postIngredientDto.getStoreId())
                .orElseThrow(() -> new EntityNotFoundException("Store not found with id: " + postIngredientDto.getStoreId()));
        VendorEntity vendor = vendorRepository.findById(postIngredientDto.getVendorId())
                .orElseThrow(() -> new EntityNotFoundException("Vendor not found with id: " + postIngredientDto.getVendorId()));
        IngredientScaleEntity scale = scaleRepository.findById(postIngredientDto.getScaleId())
                .orElseThrow(() -> new EntityNotFoundException("Scale not found with id: " + postIngredientDto.getScaleId()));

        ingredient.setStore(store);
        ingredient.setVendor(vendor);
        ingredient.setScale(scale);
        ingredientRepository.save(ingredient);
    }


    public IngredientDto updateIngredient(Integer ingredientId, PatchIngredientDto patchIngredientDto) {
        IngredientEntity ingredientEntity = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new RuntimeException("Ingredient not found with id " + ingredientId));
        // Null 체크를 추가하여 값이 있는 경우에만 업데이트
        if (patchIngredientDto.getIngredientName() != null) {
            ingredientEntity.setIngredientName(patchIngredientDto.getIngredientName());
        }
        if ((Integer)patchIngredientDto.getIngredientPrice() != null) {
            ingredientEntity.setIngredientPrice(patchIngredientDto.getIngredientPrice());
        }
        if (patchIngredientDto.getIngredientCapacity() != null) {
            ingredientEntity.setIngredientCapacity(patchIngredientDto.getIngredientCapacity());
        }
        if (patchIngredientDto.getMinimumCount() != null) {
            ingredientEntity.setMinimumCount(patchIngredientDto.getMinimumCount());
        }
        if (patchIngredientDto.getDeliveryTime() != null) {
            ingredientEntity.setDeliveryTime(patchIngredientDto.getDeliveryTime());
        }
        if (patchIngredientDto.getOrderCount() != null) {
            ingredientEntity.setOrderCount(patchIngredientDto.getOrderCount());
        }
        ingredientEntity.setUpdatedAt(LocalDateTime.now());

        ingredientRepository.save(ingredientEntity);
        return fromEntity(ingredientEntity);
    }

    public void deleteIngredient(Integer ingredientId) {
        IngredientEntity ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new EntityNotFoundException("Ingredient not found with id: " + ingredientId));
        ingredientRepository.delete(ingredient); //deleteById로 바로해도되지만, 없는 상황을 대비.
    }

    public IngredientDto findIngredientById(Integer ingredientId) {
        IngredientEntity ingredient = ingredientRepository.findById(ingredientId)
        .orElseThrow(() -> new EntityNotFoundException("Ingredient not found with id: " + ingredientId));

        return fromEntity(ingredient);
    }

    public List<IngredientDto> findAllIngredients() {
        List<IngredientEntity> ingredients = ingredientRepository.findAll();
        return ingredients.stream()
                .map(this::fromEntity)
                .collect(Collectors.toList());
    }

    //Entity -> Dto 로 변환
    private IngredientDto fromEntity(IngredientEntity entity) {
        return IngredientDto.builder()
                .ingredientId(entity.getIngredientId())
                .storeId(entity.getStore().getStoreId())
                .vendorId(entity.getVendor().getVendorId())
                .scaleId(entity.getScale().getScaleId())
                .ingredientName(entity.getIngredientName())
                .ingredientPrice(entity.getIngredientPrice())
                .ingredientCapacity(entity.getIngredientCapacity())
                .minimumCount(entity.getMinimumCount())
                .deliveryTime(entity.getDeliveryTime())
                .build();
    }

    //Dto -> Entity 변환
    private IngredientEntity convertToEntity(IngredientDto dto) {
        IngredientEntity entity = new IngredientEntity();
        entity.setIngredientId(dto.getIngredientId());
        entity.setIngredientName(dto.getIngredientName());
        entity.setIngredientPrice(dto.getIngredientPrice());
        entity.setIngredientCapacity(dto.getIngredientCapacity());
        entity.setMinimumCount(dto.getMinimumCount());
        entity.setDeliveryTime(dto.getDeliveryTime());
        return entity;
    }
}
