package e204.autocazing.db.repository;

import e204.autocazing.db.entity.RestockOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestockOrderRepository extends JpaRepository<RestockOrderEntity, Integer> {
}
