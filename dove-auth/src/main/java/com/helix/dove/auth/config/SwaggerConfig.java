package com.helix.dove.auth.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI doveAuthOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Dove Auth Service API")
                .description("Authentication and Authorization Service for Dove Platform")
                .version("1.0.0")
                .license(new License()
                    .name("Apache 2.0")
                    .url("http://www.apache.org/licenses/LICENSE-2.0.html")));
    }
} 