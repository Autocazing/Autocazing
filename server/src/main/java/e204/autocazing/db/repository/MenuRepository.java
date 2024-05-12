package e204.autocazing.db.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import e204.autocazing.db.entity.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<MenuEntity, Integer> {

    @Query(value = "SELECT new map(DATE(o.createdAt) as date, SUM(os.menuQuantity) as totalSales) "
            + "FROM OrderEntity o JOIN o.orderSpecific os "
            + "WHERE o.createdAt >= :startDate AND os.menuId = :menuId "
            + "GROUP BY DATE(o.createdAt) "
            + "ORDER BY DATE(o.createdAt)")
    List<Map<String, Object>> calculateDailySales(@Param("startDate") LocalDateTime startDate, @Param("menuId") Integer menuId);

    @Query(value = "SELECT new map(WEEK(o.createdAt) as week, YEAR(o.createdAt) as year, SUM(os.menuQuantity) as totalSales) "
            +"FROM OrderEntity o JOIN o.orderSpecific os "
            +"WHERE o.createdAt >= :startDate AND os.menuId = :menuId "
            +"GROUP BY WEEK(o.createdAt), YEAR(o.createdAt) "
            +"ORDER BY YEAR(o.createdAt), WEEK(o.createdAt)")
    List<Map<String, Object>> calculateWeekSales(@Param("startDate") LocalDateTime startDate, @Param("menuId") Integer menuId);

    @Query(value = "SELECT new map(MONTH(o.createdAt) as month, YEAR(o.createdAt) as year, SUM(os.menuQuantity) as totalSales) "
            +"FROM OrderEntity o JOIN o.orderSpecific os "
            +"WHERE o.createdAt >= :startDate AND os.menuId = :menuId "
            +"GROUP BY MONTH(o.createdAt), YEAR(o.createdAt) "
            +"ORDER BY YEAR(o.createdAt), MONTH(o.createdAt)")
    List<Map<String, Object>> calculateMonthSales(@Param("startDate") LocalDateTime startDate, @Param("menuId") Integer menuId);


    MenuEntity findByMenuId(Integer menuId);
}

