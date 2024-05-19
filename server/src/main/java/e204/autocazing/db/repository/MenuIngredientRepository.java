package e204.autocazing.db.repository;

import e204.autocazing.db.entity.IngredientEntity;
import e204.autocazing.db.entity.MenuIngredientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuIngredientRepository extends JpaRepository<MenuIngredientEntity, Integer> {
    @Query("SELECT mi.ingredient FROM MenuIngredientEntity mi WHERE mi.menu.menuId = :menuId")
    List<IngredientEntity> findIngredientsByMenuId(@Param("menuId") Integer menuId);
}