package e204.autocazing.db.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import e204.autocazing.db.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {
	@Query(value = "SELECT new map(DATE(o.createdAt) as date, SUM(os.menuQuantity * os.menuPrice) as totalSales) "
		+ "FROM OrderEntity o JOIN o.orderSpecific os "
		+ "WHERE o.createdAt >= :startDate "
		+ "GROUP BY DATE(o.createdAt) "
		+ "ORDER BY DATE(o.createdAt)")
	List<Map<String, Object>> calculateDailySales(LocalDateTime startDate);

	@Query(value = "SELECT new map(WEEK(o.createdAt) as week, YEAR(o.createdAt) as year, SUM(os.menuQuantity * os.menuPrice) as totalSales) "
		+"FROM OrderEntity o JOIN o.orderSpecific os "
		+"WHERE o.createdAt >= :startDate "
		+"GROUP BY WEEK(o.createdAt), YEAR(o.createdAt) "
		+"ORDER BY YEAR(o.createdAt), WEEK(o.createdAt)")
	List<Map<String, Object>> calculateWeekSales(LocalDateTime startDate);

	@Query(value = "SELECT new map(MONTH(o.createdAt) as month, YEAR(o.createdAt) as year, SUM(os.menuQuantity * os.menuPrice) as totalSales) "
		+"FROM OrderEntity o JOIN o.orderSpecific os "
		+"WHERE o.createdAt >= :startDate "
		+"GROUP BY MONTH(o.createdAt), YEAR(o.createdAt) "
		+"ORDER BY YEAR(o.createdAt), MONTH(o.createdAt)")
	List<Map<String, Object>> calculateMonthSales(LocalDateTime startDate);

	@Query(value = "SELECT SUM(os.menuQuantity) as totalSales "
		+"FROM OrderEntity o JOIN o.orderSpecific os "
		+"WHERE DATE(o.createdAt) = :today ")
	Integer getSoldNumber(LocalDate today);

	// @Query(value = "SELECT dayOfWeek, AVG(totalSales) as averageSales "
	// 	+ "FROM ( "
	// 	+ "SELECT DAYNAME(o.createdAt) as dayOfWeek, os.menuQuantity * os.menuPrice as totalSales "
	// 	+ "OrderEntity o "
	// 	+ "JOIN orderSpecifics os ON o.orderId = os.orderId "
	// 	+ "WHERE o.createdAt BETWEEN :startDate AND :endDate "
	// 	+ ") as dailySales "
	// 	+ "GROUP BY dayOfWeek "
	// 	+ "ORDER BY FIELD(dayOfWeek, 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday')")
	// List<Map<String, Object>> getAvgSales(LocalDateTime startDate, LocalDateTime endDate);

	@Query(value = "SELECT DAYNAME(o.createdAt) as dayOfWeek, os.menuQuantity * os.menuPrice as totalSales "
		+"FROM OrderEntity o JOIN o.orderSpecific os "
		+"WHERE o.createdAt BETWEEN :startDate AND :endDate ")
	List<Map<String, Object>> getAvgSales(LocalDateTime startDate, LocalDateTime endDate);
}