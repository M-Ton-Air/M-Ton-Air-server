package com.polytech.mtonairserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Tag;
import springfox.documentation.service.Tags;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.HashSet;

@Configuration
@EnableSwagger2
public class SwaggerConfig
{

    public static final String USERS_NAME_TAG = "Users";
    private static final String USERS_DESCRIPTION_TAG = "M-Ton-Air Users Management API";

    @Bean
    /**
     * M-Ton-Air docker api config
     * https://openclassrooms.com/fr/courses/4668056-construisez-des-microservices/5123565-documentez-votre-microservice-avec-swagger-2
     * https://www.baeldung.com/swagger-2-documentation-for-spring-rest-api
     *
     */
    public Docket mTonAirApi() throws IllegalAccessException, InstantiationException
    {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .tags(new Tag(USERS_NAME_TAG, USERS_DESCRIPTION_TAG));

    }

}
