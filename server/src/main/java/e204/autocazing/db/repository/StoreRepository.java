package e204.autocazing.db.repository;

import e204.autocazing.db.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity,Integer> {

    StoreEntity findByStoreId();
}
