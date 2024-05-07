package e204.autocazing.db.repository;

import java.time.LocalDateTime;

import e204.autocazing.db.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity.OrderSpecific, Integer> {

	@Query("SELECT SUM(os.menuQuantity * os.menuPrice) FROM OrderSpecific os WHERE os.order.createdAt >= :startDate")
	Long calculateSalesSince(LocalDateTime startDate);
}
