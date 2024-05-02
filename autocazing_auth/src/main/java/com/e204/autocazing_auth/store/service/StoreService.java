package com.e204.autocazing_auth.store.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.e204.autocazing_auth.db.entity.StoreEntity;
import com.e204.autocazing_auth.db.repository.StoreRepository;
import com.e204.autocazing_auth.store.dto.StoreRequestDto;

@Service
public class StoreService {
	StoreRepository storeRepository;
	BCryptPasswordEncoder passwordEncoder;

	@Autowired
	public StoreService(StoreRepository storeRepository, BCryptPasswordEncoder passwordEncoder) {
		this.storeRepository = storeRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public StoreEntity createStore(StoreRequestDto storeRequestDto) {

		ModelMapper mapper = new ModelMapper();
		StoreEntity storeEntity = mapper.map(storeRequestDto, StoreEntity.class);
		storeEntity.setPassword(passwordEncoder.encode(storeRequestDto.getPassword())); // 비밀번호를 암호화
		storeRepository.save(storeEntity);

		return storeEntity;
	}
}

