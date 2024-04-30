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
			.route(r -> r.path("/test1/**")
				.filters(f -> f.addRequestHeader("first-request","first-request-header2")
					.addResponseHeader("first-response","first-response-header2"))
				.uri("lb://Test1Discovery"))
			.route(r -> r.path("/test2/**")
				.filters(f -> f.addRequestHeader("second-request","second-request-header2")
					.addResponseHeader("second-response","second-response-header2"))
				.uri("lb://Test2Discovery"))
			.build();
	}
}