package io.kadmos.savings.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.kadmos.savings.util.ObjectMapperUtil;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class RedissonConfig {

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient(RedisProperties redisProperties,
                                         RedissonConfigProperties properties) {
        Config config = new Config();
        config.setLockWatchdogTimeout(5000)
                .useSingleServer()
                .setAddress(redisProperties.getUrl())
                .setPassword(redisProperties.getPassword());
        log.info("Redisson is starting with redisProperties: {}", ObjectMapperUtil.toJson(redisProperties));
        return Redisson.create(config);
    }
}
