package com.e204.autocazing_apigateway;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {
	public CustomFilter() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		//Pre Filter
		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			ServerHttpResponse response = exchange.getResponse();

			log.info("Custom PRE filter: request uri -> {}", request.getId());
			//System.out.println("Custom PRE filter: request uri : "+ request.getId());

			//Custom Post Filter
			return chain.filter(exchange).then(Mono.fromRunnable(() -> {
				log.info("Custom POST filter: response code -> {}", response.getStatusCode());
				//System.out.println("Custom POST filter: response code : "+ response.getStatusCode());
				//System.out.println("Custom POST filter: response token : "+ response.getHeaders().get("token"));
			}));
		};
	}

	public static class Config {

	}
}
