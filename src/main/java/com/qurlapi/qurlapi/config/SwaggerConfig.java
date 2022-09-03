package com.qurlapi.qurlapi.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${springdoc.swagger-ui.path}")
    private String baseUrl;

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI().info(new Info().title("Quick Url shortener")
                                            .description("API for url shortening")
                                            .version("0.1.0")
                                            .license(new License().name("GPL-3.0 License")
                                                                  .url("https://github.com/kujawad/q-url-api/blob" +
                                                                       "/master/LICENSE")))
                            .externalDocs(new ExternalDocumentation().description("Github contact")
                                                                     .url("https://github.com/kujawad/docs"));
    }
}