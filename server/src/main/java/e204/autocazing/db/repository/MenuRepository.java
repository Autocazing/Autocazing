package e204.autocazing.db.repository;

import e204.autocazing.db.entity.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<MenuEntity, Integer> {

    MenuEntity findByMenuName(String menuName);
}
