package com.e204.autocazing_auth.store.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.e204.autocazing_auth.db.entity.StoreEntity;
import com.e204.autocazing_auth.store.dto.StoreDto;
import com.e204.autocazing_auth.store.service.StoreService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class StoreController {

	// private final PasswordEncoder passwordEncoder;
	// private final JwtTokenProvider jwtTokenProvider;
	// private final StoreRepository storeRepository;
	private final StoreService storeService;

	@PostMapping("/register")
	public StoreEntity register(@RequestBody StoreDto storeDto) {
		return storeService.createStore(storeDto);
	}

	// @PostMapping("/login")
	// public String login(@RequestBody Map<String, String> store) {
	// 	StoreEntity storeEntity = storeRepository.findByLoginId(store.get("loginId"))
	// 		.orElseThrow(() -> new IllegalArgumentException("가입 되지 않은 아이디입니다"));
	// 	if (!passwordEncoder.matches(store.get("password"), storeEntity.getPassword())) {
	// 		throw new IllegalArgumentException("이메일 또는 비밀번호가 맞지 않습니다.");
	// 	}
	//
	// 	return jwtTokenProvider.createToken(storeEntity.getLoginId());
	// }
}
