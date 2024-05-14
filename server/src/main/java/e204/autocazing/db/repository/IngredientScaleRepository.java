package e204.autocazing.db.repository;

import java.util.List;

import e204.autocazing.db.entity.IngredientScaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientScaleRepository extends JpaRepository<IngredientScaleEntity, Integer> {

    IngredientScaleEntity findByUnitAndStoreId(String unit, Integer storeId);

    List<IngredientScaleEntity> findAllByStoreId(Integer storeId);
}
