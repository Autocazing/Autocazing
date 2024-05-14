package com.e204.autocazing_alert.db.repository;

import com.e204.autocazing_alert.db.entity.AlertEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;



@Repository
public interface AlertRepository extends JpaRepository<AlertEntity, Integer> {
    List<AlertEntity> findByLoginId(String loginId);
}
