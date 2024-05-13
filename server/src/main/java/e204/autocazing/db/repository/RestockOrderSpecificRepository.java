package e204.autocazing.db.repository;

import e204.autocazing.db.entity.RestockOrderSpecificEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestockOrderSpecificRepository extends JpaRepository<RestockOrderSpecificEntity, Integer> {

    List<RestockOrderSpecificEntity> findByRestockOrderRestockOrderId(Integer restockOrderId);
}
