package e204.autocazing.db.repository;

import e204.autocazing.db.entity.OrderSpecificEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderSpecificRepository extends JpaRepository<OrderSpecificEntity, Integer> {
}
