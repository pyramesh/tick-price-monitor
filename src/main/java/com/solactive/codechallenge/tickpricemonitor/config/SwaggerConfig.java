package com.solactive.codechallenge.tickpricemonitor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Ramesh.Yaleru on 05/02/2021
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig implements WebMvcConfigurer{

    @Bean
    public Docket immigrationFileMicroServiceAPI() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.solactive.codechallenge.tickpricemonitor.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metaInfo())
                .securitySchemes(Collections.singletonList(apiKey()))
                .useDefaultResponseMessages(false)
                .securityContexts(Arrays.asList(securityContext()));
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("/api.*"))
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("apiKey", authorizationScopes));
    }

    private ApiInfo metaInfo() {
        ApiInfo info = new ApiInfo("REST services are exposed to manage the price statistics",
                "REST services are exposed to manage the price statistics",
                "1.0", "terms of service",
                "",
                "", "apache licensing URL");
        return info;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

    }

    private ApiKey apiKey() {
        //return new ApiKey("AUTHORIZATION", "Authorization", "header");
        return new ApiKey("apiKey", "Authorization", "header");

    }

}
