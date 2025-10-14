package com.studio.settlement.common.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor())
                .addPathPatterns("/**") // 拦截所有路径
                .excludePathPatterns( // 排除不需要拦截的路径
                        // Swagger 和 API 文档相关的路径
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**",
                        "/doc.html",
                        "/webjars/**",
                        "/swagger-resources/**",
                        "/api-docs/**",
                        // 通常你的登录接口也需要排除，确保能正常登录获取Token
                        "/settlement/login/login"
                );
    }
}
