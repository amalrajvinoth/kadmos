package io.kadmos.savings;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class SavingsApplication {
    public static void main(String[] args) {
        SpringApplication.run(SavingsApplication.class, args);
    }
}
