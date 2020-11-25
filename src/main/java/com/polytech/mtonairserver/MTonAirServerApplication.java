package com.polytech.mtonairserver;

import com.polytech.mtonairserver.customexceptions.ExceptionLogger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//@SpringBootApplication
@EnableSwagger2
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableCaching
public class MTonAirServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MTonAirServerApplication.class, args);
    }

}
