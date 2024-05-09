package e204.autocazing.db.repository;

import e204.autocazing.db.entity.IngredientEntity;
import e204.autocazing.db.entity.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<StockEntity, Integer> {

    //유통기한이 조금 남은것부터 조회
    List<StockEntity> findByIngredientIngredientIdOrderByExpirationDateAsc(Integer ingredientId);


    StockEntity findByIngredient(IngredientEntity ingredient);

    //유통기한 상관없이 모든 재고 가져오기.
    @Query("SELECT COALESCE(SUM(s.quantity), 0) FROM StockEntity s WHERE s.ingredient.id = :ingredientId")
    Integer findTotalQuantityByIngredientId(Integer ingredientId);

    void deleteByExpirationDateBefore(LocalDate today);

    @Query("SELECT COALESCE(SUM(s.quantity), 0) FROM StockEntity s WHERE s.ingredient.id = :ingredientId")
    int sumQuantityByIngredient(Integer ingredientId);

    @Query("SELECT COUNT(s) FROM StockEntity s WHERE s.ingredient.id = :ingredientId AND s.expirationDate < :date")
    int countByIngredientAndExpirationDateBefore(Integer ingredientId, LocalDate date);
}
