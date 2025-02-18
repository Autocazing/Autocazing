package e204.autocazing.db.repository;

import e204.autocazing.db.entity.IngredientEntity;
import e204.autocazing.db.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<IngredientEntity, Integer> {
	@Query(value = "SELECT i.vender.venderManagerContact FROM IngredientEntity i "
		+ "WHERE i.ingredientId = :ingredientId AND i.store.StoreId = :storeId ")
	String findContactByIngredientId(Integer ingredientId, Integer storeId);

	@Query(value = "SELECT i.ingredientName FROM IngredientEntity i "
		+ "WHERE i.ingredientId = :ingredientId AND i.store.StoreId = :storeId ")
	String findNameByIngredientId(Integer ingredientId, Integer storeId);

    List<IngredientEntity> findByStore(StoreEntity store);

	@Query(value = "SELECT i.vender.venderId FROM IngredientEntity i WHERE i.ingredientId=:ingredientId ")
	Integer findByIngredientId(Integer ingredientId);

	IngredientEntity findIngredientByIngredientId(Integer ingredientId);

	@Query("SELECT i.ingredientName FROM IngredientEntity i WHERE i.ingredientId = :ingredientId")
	String findIngredientNameByIngredientId(@Param("ingredientId") Integer ingredientId);
	@Query("SELECT i.ingredientCapacity FROM IngredientEntity i WHERE i.ingredientId = :ingredientId")
	Integer findIngredientCapacityByIngredientId(@Param("ingredientId") Integer ingredientId);

	List<IngredientEntity> findAllByStore(StoreEntity storeEntity);
}
