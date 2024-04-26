package com.polarbookshop.catalogservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJdbcAuditing
public class DataConfig {
    @Bean
    AuditorAware<String> auditorAware() {
        return () ->
                Optional.ofNullable(SecurityContextHolder.getContext()) // 현재 인증된 사용자 정보를 위해 SecurityContextHolder 에서 SecurityContext 를 추출
                        .map(SecurityContext::getAuthentication) // SecurityContext 에서 Authentication 을 추출
                        .filter(Authentication::isAuthenticated) // 혹시모를 사용자가 인증되지 않았으나 데이터를 변경하려는 경우를 처리하기 위함
                        .map(Authentication::getName); // Authentication 에서 인증된 사용자 이름을 추출
    }
}
