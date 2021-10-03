package com.irka.dizaynifo.fileservice.config;

import com.irka.infrastructure.cache.CacheManager;
import com.irka.infrastructure.security.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtFilterConfig {

    @Bean
    @Qualifier("jwtFilter")
    JwtFilter jwtFilter(@Qualifier("redisCacheManager") CacheManager cacheManager) {
        return new JwtFilter(cacheManager);
    }
}