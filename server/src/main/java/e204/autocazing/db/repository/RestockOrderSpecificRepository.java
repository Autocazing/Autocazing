package e204.autocazing.db.repository;

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

    // @Query("SELECT r.restockOrderSpecific FROM RestockOrderEntity r WHERE r.restockOrderId = :restockOrderId ")
    // List<RestockOrderSpecificEntity> findOrderSpecificByRestockOrderId(Integer restockOrderId);

}
