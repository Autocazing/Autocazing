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
	private final StoreService storeService;

	@PostMapping("/register")
	public StoreEntity register(@RequestBody StoreDto storeDto) {
		return storeService.createStore(storeDto);
	}

}
