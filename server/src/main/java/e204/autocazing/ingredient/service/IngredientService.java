package e204.autocazing.ingredient.service;

import e204.autocazing.db.entity.IngredientEntity;
import e204.autocazing.db.entity.IngredientScaleEntity;
import e204.autocazing.db.entity.StoreEntity;
import e204.autocazing.db.entity.VenderEntity;
import e204.autocazing.db.repository.IngredientRepository;
import e204.autocazing.db.repository.IngredientScaleRepository;
import e204.autocazing.db.repository.StoreRepository;
import e204.autocazing.db.repository.VenderRepository;
import e204.autocazing.ingredient.dto.IngredientDetails;
import e204.autocazing.ingredient.dto.PatchIngredientDto;
import e204.autocazing.ingredient.dto.PostIngredientDto;
import e204.autocazing.ingredient.dto.ScaleDto;
import e204.autocazing.scale.dto.PostIngredientScaleDto;
import e204.autocazing.scale.service.IngredientScaleService;
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
    private VenderRepository venderRepository;
    @Autowired
    private IngredientScaleRepository scaleRepository;
    @Autowired
    private IngredientScaleService ingredientScaleService;

    public void createIngredient(PostIngredientDto postIngredientDto) {
        IngredientEntity ingredient = new IngredientEntity();
        ingredient.setIngredientName(postIngredientDto.getIngredientName());
        ingredient.setIngredientPrice(postIngredientDto.getIngredientPrice());
        ingredient.setIngredientCapacity(postIngredientDto.getIngredientCapacity());
        ingredient.setMinimumCount(postIngredientDto.getMinimumCount());
        ingredient.setDeliveryTime(postIngredientDto.getDeliveryTime());
        ingredient.setOrderCount(postIngredientDto.getOrderCount());
        ingredient.setImageUrl(postIngredientDto.getImageUrl());

        // 참조 설정
        StoreEntity store = storeRepository.findById(postIngredientDto.getStoreId())
            .orElseThrow(() -> new EntityNotFoundException("Store not found with id: " + postIngredientDto.getStoreId()));
        VenderEntity vender = venderRepository.findById(postIngredientDto.getVenderId())
            .orElseThrow(() -> new EntityNotFoundException("Vender not found with id: " + postIngredientDto.getVenderId()));
        //단위 직접 입력
        if(postIngredientDto.getScale().getScaleId() == 0){
            //IngredientScale DB에 새로운 데이터 추가
            IngredientScaleEntity ingredientScaleEntity = new IngredientScaleEntity();
            ingredientScaleEntity.setUnit(postIngredientDto.getScale().getUnit());
            IngredientScaleEntity scaleEntity = scaleRepository.save(ingredientScaleEntity);
            ingredient.setScale(scaleEntity);
        }
        //이미 있는 단위라면
        else{
            IngredientScaleEntity scaleEntity = scaleRepository.findById(postIngredientDto.getScale().getScaleId())
                .orElseThrow(() -> new EntityNotFoundException("Scale not found with id: " + postIngredientDto.getScale().getScaleId()));
            ingredient.setScale(scaleEntity);
        }

        ingredient.setStore(store);
        ingredient.setVender(vender);
        ingredientRepository.save(ingredient);
    }


    public IngredientDetails updateIngredient(Integer ingredientId, PatchIngredientDto patchIngredientDto) {
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
        if(patchIngredientDto.getImageUrl() != null){
            ingredientEntity.setImageUrl(patchIngredientDto.getImageUrl());
        }
        if(patchIngredientDto.getScale() != null){
            //단위 직접 입력
            if(patchIngredientDto.getScale().getScaleId() == 0) {
                PostIngredientScaleDto postIngredientScaleDto = new PostIngredientScaleDto();
                postIngredientScaleDto.setUnit(patchIngredientDto.getScale().getUnit());
                ingredientScaleService.createIngredientScale(postIngredientScaleDto);
            }
            else{
                IngredientScaleEntity scaleEntity = scaleRepository.findById(patchIngredientDto.getScale().getScaleId())
                    .orElseThrow(() -> new RuntimeException("ingredientScaleId not found with id " + patchIngredientDto.getScale().getScaleId()));
                ingredientEntity.setScale(scaleEntity);
            }

        }
        if(patchIngredientDto.getVenderId() != null){
            VenderEntity venderEntity = venderRepository.findById(patchIngredientDto.getVenderId())
                .orElseThrow(() -> new RuntimeException("venderId not found with id " + patchIngredientDto.getVenderId()));
            ingredientEntity.setVender(venderEntity);
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

    public IngredientDetails findIngredientById(Integer ingredientId) {
        IngredientEntity ingredient = ingredientRepository.findById(ingredientId)
            .orElseThrow(() -> new EntityNotFoundException("Ingredient not found with id: " + ingredientId));



        return fromEntity(ingredient);
    }

    public List<IngredientDetails> findAllIngredients() {
        List<IngredientEntity> ingredients = ingredientRepository.findAll();
        return ingredients.stream()
            .map(this::fromEntity)
            .collect(Collectors.toList());
    }

    //Entity -> Dto 로 변환
    private IngredientDetails fromEntity(IngredientEntity entity) {

        ScaleDto scaleDto = new ScaleDto();
        scaleDto.setScaleId(entity.getScale().getScaleId());
        scaleDto.setUnit(entity.getScale().getUnit());
        return IngredientDetails.builder()
            .ingredientId(entity.getIngredientId())
            .venderId(entity.getVender().getVenderId())
            .venderName(entity.getVender().getVenderName())
            .scale(scaleDto)
            .ingredientName(entity.getIngredientName())
            .ingredientPrice(entity.getIngredientPrice())
            .ingredientCapacity(entity.getIngredientCapacity())
            .minimumCount(entity.getMinimumCount())
            .orderCount(entity.getOrderCount())
            .deliveryTime(entity.getDeliveryTime())
            .imageUrl(entity.getImageUrl())
            .build();
    }
}