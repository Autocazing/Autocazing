package e204.autocazing.db.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import e204.autocazing.db.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {
	@Query(value = "SELECT new map(DATE(o.createdAt) as date, SUM(os.menuQuantity * os.menuPrice) as totalSales) "
			+ "FROM OrderEntity o JOIN o.orderSpecific os "
			+ "WHERE o.createdAt >= :startDate AND o.store.StoreId = :storeId "
			+ "GROUP BY DATE(o.createdAt) "
			+ "ORDER BY DATE(o.createdAt)")
	List<Map<String, Object>> calculateDailySales(LocalDateTime startDate, Integer storeId);

	@Query(value = "SELECT new map(WEEK(o.createdAt) as week, YEAR(o.createdAt) as year, SUM(os.menuQuantity * os.menuPrice) as totalSales) "
			+"FROM OrderEntity o JOIN o.orderSpecific os "
			+"WHERE o.createdAt >= :startDate AND o.store.StoreId = :storeId "
			+"GROUP BY WEEK(o.createdAt), YEAR(o.createdAt) "
			+"ORDER BY YEAR(o.createdAt), WEEK(o.createdAt)")
	List<Map<String, Object>> calculateWeekSales(LocalDateTime startDate, Integer storeId);

	@Query(value = "SELECT new map(MONTH(o.createdAt) as month, YEAR(o.createdAt) as year, SUM(os.menuQuantity * os.menuPrice) as totalSales) "
			+"FROM OrderEntity o JOIN o.orderSpecific os "
			+"WHERE o.createdAt >= :startDate AND o.store.StoreId = :storeId "
			+"GROUP BY MONTH(o.createdAt), YEAR(o.createdAt) "
			+"ORDER BY YEAR(o.createdAt), MONTH(o.createdAt)")
	List<Map<String, Object>> calculateMonthSales(LocalDateTime startDate, Integer storeId);

	@Query(value = "SELECT 'todaySold' as day, SUM(os.menuQuantity) as totalSales " +
		"FROM OrderEntity o JOIN o.orderSpecific os " +
		"WHERE DATE(o.createdAt) = :today AND o.store.StoreId = :storeId " +
		"UNION ALL " +
		"SELECT 'yesterdaySold' as day, SUM(os.menuQuantity) as totalSales " +
		"FROM OrderEntity o JOIN o.orderSpecific os " +
		"WHERE DATE(o.createdAt) = :yesterday AND o.store.StoreId = :storeId ")
	List<Map<String, Object>> getSalesByDay(@Param("today") LocalDate today, @Param("yesterday") LocalDate yesterday, @Param("storeId") Integer storeId);


	@Query(value = "SELECT DAYNAME(o.createdAt) as dayOfWeek, os.menuQuantity * os.menuPrice as totalSales "
			+"FROM OrderEntity o JOIN o.orderSpecific os "
			+"WHERE o.createdAt BETWEEN :startDate AND :endDate AND o.store.StoreId = :storeId ")
	List<Map<String, Object>> getAvgSales(LocalDateTime startDate, LocalDateTime endDate, Integer storeId);

}