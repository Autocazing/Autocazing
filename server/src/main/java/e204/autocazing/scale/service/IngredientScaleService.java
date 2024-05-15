package e204.autocazing.scale.service;

import e204.autocazing.db.entity.IngredientScaleEntity;
import e204.autocazing.db.repository.IngredientScaleRepository;
import e204.autocazing.db.repository.StoreRepository;
import e204.autocazing.scale.dto.IngredientScaleDto;
import e204.autocazing.scale.dto.PatchIngredientScaleDto;
import e204.autocazing.scale.dto.PostIngredientScaleDto;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngredientScaleService {
    @Autowired
    private IngredientScaleRepository ingredientScaleRepository;
    @Autowired
    private StoreRepository storeRepository;

    public void createIngredientScale(PostIngredientScaleDto postScaleDto, String loginId) {
        Integer storeId = storeRepository.findStoreIdByLoginId(loginId);

        // DB에서 해당 Unit이 있는지 확인
        IngredientScaleEntity existingScale = ingredientScaleRepository.findByUnitAndStoreId(postScaleDto.getUnit(), storeId);

        // 해당 Unit이 없으면 새로운 Entity 생성 및 저장
        if (existingScale == null) {
            IngredientScaleEntity newScale = new IngredientScaleEntity();
            newScale.setUnit(postScaleDto.getUnit());
            newScale.setStoreId(storeId);
            ingredientScaleRepository.save(newScale);
        }
    }

    public IngredientScaleDto updateIngredientScale(Integer scaleId, PatchIngredientScaleDto patchIngredientScaleDto) {
        IngredientScaleEntity patchScale = ingredientScaleRepository.findById(scaleId)
                .orElseThrow(() -> new RuntimeException("Scale not found with id " + scaleId));
        patchScale.setUnit(patchIngredientScaleDto.getUnit());
        ingredientScaleRepository.save(patchScale);
        return fromEntity(patchScale);

    }

    public void deleteIngredientScale(Integer scaleId) {
        ingredientScaleRepository.deleteById(scaleId);
    }

    public List<IngredientScaleDto> findAllIngredientScales(String loginId) {
        Integer storeId = storeRepository.findStoreIdByLoginId(loginId);
        List<IngredientScaleEntity> ingredientScaleEntityList = ingredientScaleRepository.findAllByStoreId(storeId);

        List<IngredientScaleDto> ingredientScaleDtoList = new ArrayList<>();

        for(IngredientScaleEntity ingredient : ingredientScaleEntityList){
            ingredientScaleDtoList.add(fromEntity(ingredient));
        }

        return ingredientScaleDtoList;
    }

    public IngredientScaleDto findIngredientScaleById(Integer scaleId) {
        IngredientScaleEntity entity = ingredientScaleRepository.findById(scaleId)
                .orElseThrow(() -> new RuntimeException("Scale not found"));
        return fromEntity(entity);
    }

    private IngredientScaleDto fromEntity(IngredientScaleEntity entity) {
        return IngredientScaleDto.builder()
                .scaleId(entity.getScaleId())
                .unit(entity.getUnit())
                .build();
    }
}
