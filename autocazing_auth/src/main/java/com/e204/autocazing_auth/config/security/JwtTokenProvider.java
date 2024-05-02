package com.e204.autocazing_auth.config.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

	//private final UserDetailsService userDetailsService;
	private final long tokenValidTime = 30 * 60 * 1000L; //30분

	public String createToken(String userPK) {
		Claims claims = Jwts.claims().setSubject(userPK);
		claims.put("roles", "ROLE_OWNER");
		Date now = new Date();
		SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + tokenValidTime))
			.signWith(key)
			.compact();
	}
}
