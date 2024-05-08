package com.e204.test1_discovery;

import java.util.Collections;
import java.util.Enumeration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/test1")
@Slf4j
public class Test1Controller {
	Environment env;

	@Autowired
	public Test1Controller(Environment env) {
		this.env = env;
	}

	@GetMapping("/welcome")
	public String welcome() {
		return "test1 입니다.";
	}

	@GetMapping("/message")
	public String message(@RequestHeader("test1-request") String header) {
		log.info(header);
		return "test1 입니다.";
	}

	@GetMapping("/check")
	public String check(HttpServletRequest request) {
		Enumeration<String> headers = request.getHeaderNames();
		Collections.list(headers).stream().forEach(name -> {
			Enumeration<String> values = request.getHeaders(name);
			Collections.list(values).stream().forEach(value -> System.out.println(name + "=" + value));
		});

		log.info("Server port={}", request.getServerPort());

		log.info("spring.cloud.client.hostname={}", env.getProperty("spring.cloud.client.hostname"));
		log.info("spring.cloud.client.ip-address={}", env.getProperty("spring.cloud.client.ip-address"));

		return String.format("test1 입니다. PORT %s", env.getProperty("local.server.port"));
	}
}
