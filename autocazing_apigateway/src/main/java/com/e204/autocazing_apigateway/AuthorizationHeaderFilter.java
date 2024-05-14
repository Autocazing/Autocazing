package com.e204.autocazing_apigateway;

import java.nio.charset.StandardCharsets;
import java.security.Key;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

	Environment env;

	@Value("${token.secret}")
	private String secret;

	public AuthorizationHeaderFilter(Environment env) {
		super(Config.class);
		this.env = env;
	}

	@Override
	public GatewayFilter apply(Config config) {
		return ((exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();

			if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION))
				return onError(exchange, "No Authorization Header", HttpStatus.UNAUTHORIZED);

			String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
			String jwt = authorizationHeader.replace("Bearer ", "");

			if (!isJwtValid(jwt))
				return onError(exchange, "Token Is Not Valid", HttpStatus.UNAUTHORIZED);

			//토큰에서 loginId 추출하기
			String loginId = getTokenSubject(jwt);

			//loginId를 request body에 넣어주기
			ServerHttpRequest includeLoginIdRequest = exchange.getRequest()
					.mutate().header("loginId", loginId).build();

			return chain.filter(exchange.mutate().request(includeLoginIdRequest).build());
		});
	}

	private String getTokenSubject(String jwt) {
		Key secretKey = Keys.hmacShaKeyFor(env.getProperty("token.secret").getBytes(StandardCharsets.UTF_8));

		return Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(jwt)
				.getBody()
				.getSubject();
	}

	private boolean isJwtValid(String jwt) {
		boolean returnValue = true;
		String subject = null;
		try {
			byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);
			subject = Jwts.parserBuilder().setSigningKey(secretBytes).build()
					.parseClaimsJws(jwt).getBody().getSubject();
		} catch (Exception exception) {
			returnValue = false;
		}

		if (subject == null || subject.isEmpty())
			returnValue = false;

		return returnValue;
	}

	private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(httpStatus);

		log.error(err);
		return response.setComplete();
	}

	public static class Config {

	}
}