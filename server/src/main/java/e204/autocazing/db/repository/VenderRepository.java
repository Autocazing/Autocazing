package e204.autocazing.db.repository;

import e204.autocazing.db.entity.VenderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VenderRepository extends JpaRepository<VenderEntity, Integer> {
}