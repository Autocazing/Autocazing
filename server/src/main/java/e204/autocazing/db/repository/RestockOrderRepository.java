package e204.autocazing.db.repository;

import e204.autocazing.db.entity.RestockOrderEntity;
import e204.autocazing.db.entity.StoreEntity;
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

    // status가 WRITING이고 특정 store에 해당하는 최신 RestockOrderEntity 조회
    Optional<RestockOrderEntity> findFirstByStatusAndStoreOrderByCreatedAtDesc(RestockOrderEntity.RestockStatus status, StoreEntity store);    @Query("SELECT r FROM RestockOrderEntity r WHERE r.status NOT IN ?1")

    List<RestockOrderEntity> findByStatusNot(@Param("statuses") Set<RestockOrderEntity.RestockStatus> statuses);

    @Query("SELECT r FROM RestockOrderEntity r WHERE r.store.StoreId = :storeId")
    List<RestockOrderEntity> findAllByStoreId(Integer storeId);

    @Query("SELECT r FROM RestockOrderEntity r WHERE r.status IN :status AND r.store.StoreId = :storeId")
    List<RestockOrderEntity> findByStatusInAndStoreId(List<RestockOrderEntity.RestockStatus> status, Integer storeId);

    List<RestockOrderEntity> findByStore(Optional<StoreEntity> storeEntity);
}
