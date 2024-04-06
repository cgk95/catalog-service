package com.polarbookshop.catalogservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "polar") // 'polar' 로 시작하는 설정 속성에 대한 소스임을 표시
public class PolarProperties {
    private String greetings;
}
