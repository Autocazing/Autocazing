package com.e204.autocazing_auth.db.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.e204.autocazing_auth.db.entity.StoreEntity;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity,Integer> {
	Optional<StoreEntity> findByLoginId(String loginId);
}
