package com.qurlapi.qurlapi.config;

import com.qurlapi.qurlapi.util.Constants;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.WebFluxRequestHandlerProvider;
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Configuration
@EnableOpenApi
@EnableWebMvc
public class SwaggerConfig implements WebMvcConfigurer {

    @Value("${springfox.documentation.swagger-ui.base-url}")
    private String baseUrl;

    @Bean
    public static BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
        return new BeanPostProcessor() {

            @Override
            public Object postProcessAfterInitialization(@NonNull final Object bean, @NonNull final String beanName)
                    throws BeansException {
                if (bean instanceof WebMvcRequestHandlerProvider || bean instanceof WebFluxRequestHandlerProvider) {
                    customizeSpringfoxHandlerMappings(getHandlerMappings(bean));
                }
                return bean;
            }

            private <T extends RequestMappingInfoHandlerMapping> void customizeSpringfoxHandlerMappings(
                    List<T> mappings) {
                mappings.removeIf(mapping -> mapping.getPatternParser() != null);
            }

            @SuppressWarnings("unchecked")
            private List<RequestMappingInfoHandlerMapping> getHandlerMappings(Object bean) {
                try {
                    Field field = ReflectionUtils.findField(bean.getClass(), "handlerMappings");
                    field.setAccessible(true);
                    return (List<RequestMappingInfoHandlerMapping>) field.get(bean);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new IllegalStateException(e);
                }
            }
        };
    }

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