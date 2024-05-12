package com.e204.autocazing_auth.store.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.e204.autocazing_auth.db.entity.StoreEntity;
import com.e204.autocazing_auth.db.repository.StoreRepository;
import com.e204.autocazing_auth.store.dto.StoreDto;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StoreService implements UserDetailsService {
	StoreRepository storeRepository;
	BCryptPasswordEncoder passwordEncoder;

	@Autowired
	public StoreService(StoreRepository storeRepository, BCryptPasswordEncoder passwordEncoder) {
		this.storeRepository = storeRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public StoreEntity createStore(StoreDto storeDto) {

		ModelMapper mapper = new ModelMapper();
		StoreEntity storeEntity = mapper.map(storeDto, StoreEntity.class);
		storeEntity.setPassword(passwordEncoder.encode(storeDto.getPassword())); // 비밀번호를 암호화
		storeRepository.save(storeEntity);

		return storeEntity;
	}

	@Override
	public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
		Optional<StoreEntity> storeEntity = storeRepository.findByLoginId(loginId);

		storeEntity.orElseThrow(() -> new UsernameNotFoundException("loginId"));
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_OWNER"));

		return new User(storeEntity.get().getLoginId(), storeEntity.get().getPassword(),
			true, true, true, true, authorities);
	}

	public StoreDto getUserDetailsByLoginId(String loginId) {
		Optional<StoreEntity> storeEntity = storeRepository.findByLoginId(loginId);
		storeEntity.orElseThrow(() -> new UsernameNotFoundException("loginId"));

		StoreDto storeDto = new StoreDto();
		storeDto.setStoreId(storeEntity.get().getStoreId());
		storeDto.setLoginId(storeEntity.get().getLoginId());
		storeDto.setPassword(storeEntity.get().getPassword());
		storeDto.setStoreName(storeEntity.get().getStoreName());
		storeDto.setCreatedAt(storeEntity.get().getCreatedAt());
		storeDto.setUpdatedAt(storeEntity.get().getUpdatedAt());

		return storeDto;
	}
}