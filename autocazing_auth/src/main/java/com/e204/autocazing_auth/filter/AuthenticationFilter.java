package com.e204.autocazing_auth.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.e204.autocazing_auth.store.dto.LoginRequestDto;
import com.e204.autocazing_auth.store.dto.StoreDto;
import com.e204.autocazing_auth.store.service.StoreService;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	private final StoreService storeService;
	private final Environment env;

	public AuthenticationFilter(AuthenticationManager authenticationManager, StoreService storeService, Environment env) {
		this.authenticationManager = authenticationManager;
		this.storeService = storeService;
		this.env = env;
		setFilterProcessesUrl("/api/users/login");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
												HttpServletResponse response) throws AuthenticationException {
		try {
			LoginRequestDto creds = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

			return getAuthenticationManager().authenticate(
				new UsernamePasswordAuthenticationToken(
					creds.getLoginId(),
					creds.getPassword(),
					new ArrayList<>()));

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request,
											HttpServletResponse response,
											FilterChain chain,
											Authentication authResult) throws IOException, ServletException {

		String username = ((User) authResult.getPrincipal()).getUsername();
		StoreDto userDetails = storeService.getUserDetailsByLoginId(username);

		Key secretKey = Keys.hmacShaKeyFor(env.getProperty("token.secret").getBytes(StandardCharsets.UTF_8));

		String token = Jwts.builder()
			.setSubject(userDetails.getStoreId().toString())
			.setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(env.getProperty("token.expiration_time"))))
			.signWith(secretKey, SignatureAlgorithm.HS512)
			.compact();

		response.addHeader("token", token);
		response.addHeader("storeId", String.valueOf(userDetails.getStoreId()));
	}

	@Override
	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}
}