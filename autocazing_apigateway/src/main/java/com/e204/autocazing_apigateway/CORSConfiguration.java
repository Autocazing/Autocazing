package com.e204.autocazing_apigateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;

import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
public class CORSConfiguration {

    private static final String ALLOWED_HEADERS = "x-requested-with, authorization, Content-Type";
    private static final String ALLOWED_METHODS = "GET, PUT, POST, DELETE, OPTIONS";

    // true 로 설정할 경우 allowed_origin 에 '*' 을 입력할 수 없고,
    //http://localhost:3000 이렇게 특정해줘야 한다.
    private static final String ALLOWED_CREDENTIALS = "true";


    @Bean
    public WebFilter corsFilter() {

        return (ServerWebExchange ctx, WebFilterChain chain) -> {

            ServerHttpRequest request = ctx.getRequest();

            if (CorsUtils.isPreFlightRequest(request)) {
                ServerHttpResponse response = ctx.getResponse();
                HttpHeaders headers = response.getHeaders();
                headers.add("Access-Control-Allow-Origin", "http://localhost:3000");
                headers.add("Access-Control-Allow-Origin", "https://k10e204.p.ssafy.io");
                headers.add("Access-Control-Allow-Methods", ALLOWED_METHODS);
                headers.add("Access-Control-Allow-Headers",ALLOWED_HEADERS);
                headers.add("Access-Control-Allow-Credentials",ALLOWED_CREDENTIALS);

                if (request.getMethod() == HttpMethod.OPTIONS) {
                    response.setStatusCode(HttpStatus.OK);
                    return Mono.empty();
                }
            }
            return chain.filter(ctx);
        };
    }

}
