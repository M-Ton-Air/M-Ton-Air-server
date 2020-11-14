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
    private static final String USERS_DESCRIPTION_TAG = "Users management API";

    public static final String AUTHENTICATION_NAME_TAG = "Authentication";
    private static final String AUTHENTICATION_DESCRIPTION_TAG = "Authentication API, for users to sign-in and/or sign-up";


    public static final String AQICN_NAME_TAG = "AQICN requests";
    private static final String AQICN_DESCRIPTION_TAG = "AQICN requests management API";

    public static final String DEFAULT_TAG = "Default fallback";
    private static final String DEFAULT_DESCRIPTION_TAG = "Handles all unsupported requests";

    public static final String DAILY_AQICN_DATA_NAME_TAG = "Daily AQICN data";
    private static final String DAILY_AQICN_DATA_DESCRIPTION_TAG = "Daily AQICN data management API";

    public static final String STATIONS_NAME_TAG = "Stations";
    private static final String STATIONS_DESCRIPTION_TAG = "Stations creation and retrieval";

    public static final String GEO_NAME_TAG = "Stations geographical coordinates";
    private static final String GEO_DESCRIPTION_TAG = "Requests all the AQICN urls and saves the different stations coordinates into a json file located in the classpath";

    public static final String USER_FAVORITE_STATION_NAME_TAG = "User favorite station";
    private static final String USER_FAVORITE_STATION_DESCRIPTION_TAG = "User favorite station management API";

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
                .tags(
                        new Tag(USERS_NAME_TAG, USERS_DESCRIPTION_TAG),
                        new Tag(AUTHENTICATION_NAME_TAG, AUTHENTICATION_DESCRIPTION_TAG),
                        new Tag(DEFAULT_TAG, DEFAULT_DESCRIPTION_TAG),
                        new Tag(AQICN_NAME_TAG, AQICN_DESCRIPTION_TAG),
                        new Tag(DAILY_AQICN_DATA_NAME_TAG, DAILY_AQICN_DATA_DESCRIPTION_TAG),
                        new Tag(STATIONS_NAME_TAG, STATIONS_DESCRIPTION_TAG),
                        new Tag(GEO_NAME_TAG, GEO_DESCRIPTION_TAG));

    }

}
