package e204.autocazing.db.repository;

import e204.autocazing.db.entity.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<StockEntity, Integer> {

    //유통기한이 조금 남은것부터 조회
    List<StockEntity> findByIngredientIngredientIdOrderByExpirationDateAsc(Integer ingredientId);

    
}
