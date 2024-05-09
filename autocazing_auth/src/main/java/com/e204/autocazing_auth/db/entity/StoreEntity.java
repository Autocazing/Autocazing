package com.e204.autocazing_auth.db.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "store")
public class StoreEntity {
	//기본키
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer StoreId;
	//로그인 Id
	@Column(nullable = false)
	private String loginId;
	//비밀번호
	@Column(nullable = false)
	private String password;
	//가게 이름
	@Column(nullable = false)
	private String storeName;
	//생성 시간
	@Column(name = "created_at", updatable = false)
	@CreationTimestamp
	private LocalDateTime createdAt;
	//수정 시간
	@Column(name = "updated_at")
	@UpdateTimestamp
	private LocalDateTime updatedAt;

	public static class Builder {
		private String loginId;
		private String password;
		private String storeName;

		public Builder loginId(String loginId) {
			this.loginId = loginId;
			return this;
		}

		public Builder password(String password) {
			this.password = password;
			return this;
		}

		public Builder storeName(String storeName) {
			this.storeName = storeName;
			return this;
		}

		public StoreEntity build() {
			StoreEntity storeEntity = new StoreEntity();
			storeEntity.loginId = this.loginId;
			storeEntity.password = this.password;
			storeEntity.storeName = this.storeName;
			storeEntity.createdAt = LocalDateTime.now();
			storeEntity.updatedAt = LocalDateTime.now();
			return storeEntity;
		}
	}

	// 빌더 객체 반환 메서드
	public static Builder builder() {
		return new Builder();
	}
	//    @OneToMany(mappedBy = "storeEntity")
	//    private List<Order> orders;
}
