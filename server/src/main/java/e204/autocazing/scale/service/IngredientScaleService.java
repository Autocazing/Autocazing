package e204.autocazing.scale.service;

import e204.autocazing.db.entity.IngredientScaleEntity;
import e204.autocazing.db.repository.IngredientScaleRepository;
import e204.autocazing.scale.dto.PatchIngredientScaleDto;
import e204.autocazing.scale.dto.PostIngredientScaleDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IngredientScaleService {
    @Autowired
    private IngredientScaleRepository ingredientScaleRepository;
    public IngredientScaleEntity createIngredientScale(PostIngredientScaleDto postScaleDto) {
        IngredientScaleEntity postScale = new IngredientScaleEntity();
        postScale.setUnit(postScaleDto.getUnit());
        return ingredientScaleRepository.save(postScale);
    }

    public IngredientScaleEntity updateIngredientScale(Integer scaleId, PatchIngredientScaleDto patchIngredientScaleDto) {
        IngredientScaleEntity patchScale = ingredientScaleRepository.findById(scaleId)
                .orElseThrow(() -> new RuntimeException("Scale not found with id " + scaleId));
        patchScale.setUnit(patchIngredientScaleDto.getUnit());
        return ingredientScaleRepository.save(patchScale);
    }

    public void deleteIngredientScale(Integer scaleId) {
        ingredientScaleRepository.deleteById(scaleId);
    }

    public List<IngredientScaleEntity> findAllIngredientScales() {
        return ingredientScaleRepository.findAll();
    }


    public IngredientScaleEntity findIngredientScaleById(Integer scaleId) {
        return ingredientScaleRepository.findById(scaleId)
                .orElseThrow(() -> new EntityNotFoundException("Scale not found with id: " + scaleId));
    }
}
