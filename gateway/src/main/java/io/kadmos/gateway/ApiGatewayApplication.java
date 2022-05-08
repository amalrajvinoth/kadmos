package io.kadmos.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {
    @Value("${jwk-jwks-uri}")
    private String jwtJwksUrl;

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity httpSecurity) {
        httpSecurity
                .authorizeExchange()
                .pathMatchers(HttpMethod.POST, "/**").hasAuthority("SCOPE_write")
                .pathMatchers(HttpMethod.GET, "/**").hasAuthority("SCOPE_read")
                .pathMatchers(HttpMethod.PUT, "/**").hasAuthority("SCOPE_write")
                .pathMatchers(HttpMethod.DELETE, "/**").hasAuthority("SCOPE_write")
                .anyExchange().authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt().jwkSetUri(jwtJwksUrl);
        return httpSecurity.build();
    }
}
