package com.sh.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ClientConfig {

    // 외부 API 요청을 위한 template
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
