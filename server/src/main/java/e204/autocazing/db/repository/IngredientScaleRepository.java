package e204.autocazing.db.repository;

import e204.autocazing.db.entity.IngredientEntity;
import e204.autocazing.db.entity.IngredientScaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientScaleRepository extends JpaRepository<IngredientScaleEntity, Integer> {
    IngredientScaleEntity findByUnit(String unit);

}
