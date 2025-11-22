package com.skhu.gdgocteambuildingproject.global.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SwaggerProperties.class)
public class SwaggerConfig {

    private static final String BEARER_KEY = "bearerAuth";

    @Bean
    public OpenAPI openAPI(SwaggerProperties swaggerProperties) {
        OpenAPI openAPI = new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(BEARER_KEY,
                                new SecurityScheme()
                                        .name(BEARER_KEY)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList(BEARER_KEY))
                .info(apiInfo());

        swaggerProperties.servers().forEach(url ->
                openAPI.addServersItem(new Server().url(url))
        );

        return openAPI;
    }

    private Info apiInfo() {
        return new Info()
                .title("GDGoC Team Building Project")
                .description("GDGoC 팀빌딩 프로젝트를 위한 스웨거입니다.")
                .version("1.0.1");
    }
}
