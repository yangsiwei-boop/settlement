package com.studio.settlement.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    // OpenAPI 主配置 (Bean名称保持唯一)
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("结算系统API")
                        .description("结算管理系统接口文档 - 基于Knife4j增强")
                        .version("1.0.0")
                );
    }

    // API分组配置 - 结算模块
    @Bean
    public GroupedOpenApi settlementApi() {
        return GroupedOpenApi.builder()
                .group("结算模块") // 分组名称
                .packagesToScan("com.studio.settlement.controller") // 扫描的Controller包
                .pathsToMatch("/settlement/**") // 匹配路径
                .build();
    }

    // 可以添加更多分组...
    @Bean
    public GroupedOpenApi defaultApi() {
        return GroupedOpenApi.builder()
                .group("默认接口")
                .pathsToMatch("/api/**")
                .build();
    }
}
