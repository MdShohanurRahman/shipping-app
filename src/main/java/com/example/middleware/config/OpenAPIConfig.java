/**
 * Created By shoh@n
 * Date: 3/28/2023
 */

package com.example.middleware.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@OpenAPIDefinition
@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        SecurityScheme bearerAuth = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        return new OpenAPI()
                .info(
                        new Info()
                                .title("shipping-rate middleware")
                                .version("1.0.0")
                                .description("api doc")
                )
                .components(
                        new Components()
                                .addSecuritySchemes("bearerAuth", bearerAuth)
                )
                .security(
                        List.of(new SecurityRequirement().addList("bearerAuth"))
                );
    }

    @Bean
    public GroupedOpenApi authenticationApi() {
        return GroupedOpenApi.builder()
                .group("authentication-api")
                .pathsToMatch("/api/v1/auth/**")
                .build();
    }

    @Bean
    public GroupedOpenApi shippingRateApi() {
        return GroupedOpenApi.builder()
                .group("shipping-rate-api")
                .pathsToMatch("/api/v1/shipping-rates/**")
                .build();
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("user-api")
                .pathsToMatch("/api/v1/users/**")
                .build();
    }
}
