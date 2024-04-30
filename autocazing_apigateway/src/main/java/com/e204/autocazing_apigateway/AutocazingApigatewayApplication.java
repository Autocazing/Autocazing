package com.e204.autocazing_apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AutocazingApigatewayApplication {
	public static void main(String[] args) {
		SpringApplication.run(AutocazingApigatewayApplication.class, args);
	}
}
