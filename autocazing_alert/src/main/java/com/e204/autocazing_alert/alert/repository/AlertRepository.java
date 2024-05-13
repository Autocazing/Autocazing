package com.e204.autocazing_alert.alert.repository;

import com.e204.autocazing_alert.alert.db.AlertEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface AlertRepository extends JpaRepository<AlertEntity, Integer > {

    List<AlertEntity> findByLoginId(String loginId);
}
