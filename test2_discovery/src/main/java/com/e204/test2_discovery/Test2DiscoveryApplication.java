package com.e204.test2_discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Test2DiscoveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(Test2DiscoveryApplication.class, args);
	}

}
