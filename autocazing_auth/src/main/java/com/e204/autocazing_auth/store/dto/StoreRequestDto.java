package com.e204.autocazing_auth.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreRequestDto {
	private Integer storeId;
	private String loginId;
	private String password;
	private String storeName;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
