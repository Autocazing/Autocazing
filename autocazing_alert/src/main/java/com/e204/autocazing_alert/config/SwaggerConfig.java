package com.e204.autocazing_alert.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Server localServer = new Server().url("http://localhost:8080").description("Local HTTP Server");
        Server httpsServer = new Server().url("https://k10e204.p.ssafy.io").description("Prod HTTPS Server");
        return new OpenAPI()
                .servers(Arrays.asList(httpsServer, localServer))
                .info(apiInfo());
    }
    private Info apiInfo() {
        return new Info()
                .title("오토카징 Alert Swagger")
                .description("오토카징의 Alert server API 명세입니다람쥐드래곤.");
    }

}
