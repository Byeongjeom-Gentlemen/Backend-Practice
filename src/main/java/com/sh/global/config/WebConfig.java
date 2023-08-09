package com.sh.global.config;

import com.sh.global.config.resolver.PageableVerificationArgumentResolver;
import com.sh.global.config.resolver.TokenArgumentResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final TokenArgumentResolver tokenArgumentResolver;
    private final PageableVerificationArgumentResolver pageableVerificationArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(tokenArgumentResolver);
        resolvers.add(pageableVerificationArgumentResolver);
    }
}
