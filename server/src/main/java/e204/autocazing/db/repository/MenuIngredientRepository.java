package e204.autocazing.db.repository;

import e204.autocazing.db.entity.MenuIngredientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuIngredientRepository extends JpaRepository<MenuIngredientEntity, Integer> {
}