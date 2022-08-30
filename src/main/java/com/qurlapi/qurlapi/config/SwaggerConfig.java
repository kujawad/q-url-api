package com.qurlapi.qurlapi.config;

import com.qurlapi.qurlapi.util.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;
import java.util.Set;

@Configuration
@EnableOpenApi
@EnableWebMvc
public class SwaggerConfig implements WebMvcConfigurer {

    @Value("${springfox.documentation.swagger-ui.base-url}")
    private String baseUrl;

    @Bean
    public Docket apiDocs() {
        return new Docket(DocumentationType.OAS_30).select()
                                                   .apis(RequestHandlerSelectors.basePackage(
                                                           "com.qurlapi.qurlapi.controller"))
                                                   .paths(PathSelectors.any())
                                                   .build()
                                                   .tags(tags())
                                                   .apiInfo(apiInfo())
                                                   .produces(applicationJson())
                                                   .consumes(applicationJson())
                                                   .useDefaultResponseMessages(false);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().license("GPL-3.0 License")
                                   .licenseUrl("https://github.com/kujawad/q-url-api/blob/master/LICENSE")
                                   .title("Quick Url shortener")
                                   .description("API for url shortening")
                                   .version("0.1")
                                   .contact(new Contact("kujawad", "https://github.com/kujawad", ""))
                                   .build();
    }

    private Tag tags() {
        return new Tag(Constants.QURL_CONTROLLER_TAG, "Controller for managing the quick urls");
    }

    private Set<String> applicationJson() {
        return Collections.singleton(MimeTypeUtils.APPLICATION_JSON_VALUE);
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        final String baseUrl = StringUtils.trimTrailingCharacter(this.baseUrl, '/');
        registry.addResourceHandler(baseUrl + "/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
                .resourceChain(false);
    }

    @Override
    public void addViewControllers(final ViewControllerRegistry registry) {
        registry.addViewController(baseUrl + "/swagger-ui/")
                .setViewName("forward:" + baseUrl + "/swagger-ui/index.html");
    }
}