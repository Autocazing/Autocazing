package e204.autocazing.db.repository;

import e204.autocazing.db.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity,Integer> {
    @Query(value = "SELECT StoreId FROM StoreEntity WHERE loginId = :loginId")
    Integer findByLoginId(String loginId);
}
