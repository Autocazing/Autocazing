package e204.autocazing.db.repository;

import e204.autocazing.db.entity.RestockOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RestockOrderRepository extends JpaRepository<RestockOrderEntity, Integer> {
    Optional<RestockOrderEntity> findFirstByStatusOrderByCreatedAtDesc(RestockOrderEntity.RestockStatus status);

//    List<RestockOrderEntity> findByStatusNot(RestockOrderEntity.RestockStatus restockStatus1, RestockOrderEntity.RestockStatus restockStatus2);
    @Query("SELECT r FROM RestockOrderEntity r WHERE r.status NOT IN ?1")
    List<RestockOrderEntity> findByStatusNot(@Param("statuses") Set<RestockOrderEntity.RestockStatus> statuses);

    List<RestockOrderEntity> findByStatusIn(List<RestockOrderEntity.RestockStatus> statuses);
}
