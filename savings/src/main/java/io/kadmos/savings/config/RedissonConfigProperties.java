package io.kadmos.savings.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Component
@NoArgsConstructor
@ConfigurationProperties(prefix = "kadmos.redisson")
public class RedissonConfigProperties {

    private Long leaseTime;
    private Long lockWatchDogTimeout;

}