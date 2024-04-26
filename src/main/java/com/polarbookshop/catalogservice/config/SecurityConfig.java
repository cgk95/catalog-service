package com.polarbookshop.catalogservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorize ->
                        authorize.mvcMatchers(HttpMethod.GET, "/", "/books/**")
                                .permitAll()
                                .anyRequest().hasRole("employee")) // 접두사는 붙이지 않아도 내부적으로 스프링 시큐리티가 처리했다굿!!
                .oauth2ResourceServer(
                        OAuth2ResourceServerConfigurer::jwt // JWT 토큰에 기반한 기본 설정을 사용해 리소스 서버 지원을 활성화한다
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 각 요청은 액세스 토큰을 가지고 있어야 하며, 그래야만 사용자가 요청간 사용자 세션을 계속 유지할 수 있다. 상태를 갖지 않기를 원한다.
                .csrf(AbstractHttpConfigurer::disable) // 인증 전략이 상태를 갖지 않고, 브라우저 기반 클라이언트가 관여되지 않기 때문에 비활성화해도 된다.
                .build();
    }

    /**
     * @return Jwt 에서 정보를 추출하여 인증된 사용자와 GrantedAuthority 객체 목록을 연결한다
     */
    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        var jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");

        var jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}
