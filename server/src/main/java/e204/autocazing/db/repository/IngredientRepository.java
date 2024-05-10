package e204.autocazing.db.repository;

import e204.autocazing.db.entity.IngredientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends JpaRepository<IngredientEntity, Integer> {
    IngredientEntity findByIngredientName(String ingredientName);
}
