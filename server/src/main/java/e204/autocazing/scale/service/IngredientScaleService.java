package e204.autocazing.scale.service;

import e204.autocazing.db.entity.IngredientScaleEntity;
import e204.autocazing.db.entity.VendorEntity;
import e204.autocazing.db.repository.IngredientScaleRepository;
import e204.autocazing.scale.dto.IngredientScaleDto;
import e204.autocazing.scale.dto.PatchIngredientScaleDto;
import e204.autocazing.scale.dto.PostIngredientScaleDto;
import e204.autocazing.vendor.dto.VendorDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IngredientScaleService {
    @Autowired
    private IngredientScaleRepository ingredientScaleRepository;
    public IngredientScaleEntity createIngredientScale(PostIngredientScaleDto postScaleDto) {
        IngredientScaleEntity postScale = new IngredientScaleEntity();
        postScale.setUnit(postScaleDto.getUnit());
        return ingredientScaleRepository.save(postScale);
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

    public List<IngredientScaleDto> findAllIngredientScales() {
        return ingredientScaleRepository.findAll().stream()
                .map(this::fromEntity)
                .collect(Collectors.toList());    }


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
