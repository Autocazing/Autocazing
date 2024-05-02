package com.e204.autocazing_auth.store.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.e204.autocazing_auth.db.repository.StoreRepository;

@Service
public class StoreService {
	StoreRepository storeRepository;
	BCryptPasswordEncoder passwordEncoder;

	@Autowired
	public StoreService(StoreRepository storeRepository, BCryptPasswordEncoder passwordEncoder) {
		this.storeRepository = storeRepository;
		this.passwordEncoder = passwordEncoder;
	}


}

