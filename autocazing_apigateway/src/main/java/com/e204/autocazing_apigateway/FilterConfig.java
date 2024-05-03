package com.e204.autocazing_apigateway;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
	@Bean
	public RouteLocator gatewayRoutes(RouteLocatorBuilder builder){
		return builder.routes()
			.route(r -> r.path("/api/menus/**","/api/stock/**","/api/ingredients/**"
					,"/api/restock/**","/api/sales/**","/api/orders/**","/api/vendors/**")
				.uri("lb://server"))
			.route(r -> r.path("/api/alerts/**")
				.uri("lb://alert-service"))
			.route(r -> r.path("/api/reports/**")
				.uri("lb://solution-service"))
			.route(r -> r.path("/api/users/**")
				.uri("lb://auth-server"))
			.build();
	}
}