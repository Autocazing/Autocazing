package e204.autocazing.db.repository;

import java.util.List;
import java.util.Optional;

import e204.autocazing.db.entity.VenderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VenderRepository extends JpaRepository<VenderEntity, Integer> {

	@Query(value = "SELECT v FROM VenderEntity v WHERE v.store.StoreId = :storeId ")
	List<VenderEntity> findAllByStoreId(Integer storeId);
}