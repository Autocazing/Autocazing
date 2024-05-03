package com.e204.autocazing_auth.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.e204.autocazing_auth.filter.AuthenticationFilter;
import com.e204.autocazing_auth.store.service.StoreService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
	private final StoreService storeService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final Environment env;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		AuthenticationManager authenticationManager = getAuthenticationFilter(http);

		http
			.csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화
			.authorizeHttpRequests(authorize -> authorize //로그인, 회원가입 api만 권한 없어도 접근 가능
				.requestMatchers("/api/users/login").permitAll()
				.requestMatchers("/api/users/register").permitAll()
				.anyRequest().authenticated()
			)
			.authenticationManager(authenticationManager)
			.addFilter(getAuthenticationFilter(authenticationManager))
			.headers(
			headersConfigurer ->
				headersConfigurer
					.frameOptions(
						HeadersConfigurer.FrameOptionsConfig::sameOrigin
					)
			);

		return http.build();
	}

	private AuthenticationManager getAuthenticationFilter(HttpSecurity http) throws Exception {

		AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.userDetailsService(storeService).passwordEncoder(bCryptPasswordEncoder);
		return authenticationManagerBuilder.build();
	}

	private AuthenticationFilter getAuthenticationFilter(AuthenticationManager authenticationManager) {
		return new AuthenticationFilter(authenticationManager, storeService, env);
	}
}
