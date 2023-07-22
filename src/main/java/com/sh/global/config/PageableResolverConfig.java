package com.sh.global.config;

import com.sh.global.util.PageableVerificationArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

// pageable page, size를 검증하고 예외처리를 하기 위한 resolver
@Configuration
public class PageableResolverConfig implements WebMvcConfigurer {

    private final PageableVerificationArgumentResolver pageableVerificationArgumentResolver;

    public PageableResolverConfig(PageableVerificationArgumentResolver pageableVerificationArgumentResolver) {
        this.pageableVerificationArgumentResolver = pageableVerificationArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(pageableVerificationArgumentResolver);
    }
}
