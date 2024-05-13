package com.e204.autocazing_alert.repository;

import com.e204.autocazing_alert.db.AlertEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository extends JpaRepository<AlertEntity, Integer> {
    List<AlertEntity> findByLoginId(String loginId);
}
