package e204.autocazing.db.repository;

import e204.autocazing.db.entity.RestockOrderEntity;
import e204.autocazing.db.entity.RestockOrderSpecificEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestockOrderSpecificRepository extends JpaRepository<RestockOrderSpecificEntity, Integer> {

    List<RestockOrderSpecificEntity> findByRestockOrderRestockOrderId(Integer restockOrderId);
    Optional<RestockOrderSpecificEntity> findByRestockOrderRestockOrderIdAndRestockOrderSpecificId(Integer restockOrderId, Integer restockOrderSpecificId);

    @Modifying
    @Query("DELETE FROM RestockOrderSpecificEntity ros WHERE ros.restockOrder.restockOrderId = :restockOrderId AND ros.restockOrderSpecificId = :specificId")
    void deleteByRestockOrderIdAndRestockOrderSpecificId(@Param("restockOrderId") Integer restockOrderId, @Param("specificId") Integer specificId);

    Boolean existsByRestockOrderAndIngredientId(RestockOrderEntity restockOrder, Integer ingredientId);

    // @Query("SELECT r.restockOrderSpecific FROM RestockOrderEntity r WHERE r.restockOrderId = :restockOrderId ")
    // List<RestockOrderSpecificEntity> findOrderSpecificByRestockOrderId(Integer restockOrderId);

    @Query("SELECT s FROM RestockOrderSpecificEntity s WHERE s.ingredientId = :ingredientId AND s.status = :status ORDER BY s.createdAt ASC")
    Optional<RestockOrderSpecificEntity> findTopByIngredientIdAndStatusOrderByCreatedAtAsc(
            @Param("ingredientId") Integer ingredientId,
            @Param("status") RestockOrderSpecificEntity.RestockSpecificStatus status
    );

    // restockOrderSpecificId로 RestockOrderEntity를 찾는 메서드 정의
    @Query("SELECT r.restockOrder FROM RestockOrderSpecificEntity r WHERE r.restockOrderSpecificId = :restockOrderSpecificId")
    RestockOrderEntity findRestockOrderByRestockOrderSpecificId(@Param("restockOrderSpecificId") Integer restockOrderSpecificId);

    // 특정 RestockOrderEntity의 모든 RestockOrderSpecificEntity를 조회하는 메서드
    List<RestockOrderSpecificEntity> findByRestockOrder_RestockOrderId(Integer restockOrderId);


}
