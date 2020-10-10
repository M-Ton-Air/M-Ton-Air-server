package com.polytech.mtonairserver;

import com.polytech.mtonairserver.customexceptions.ExceptionLogger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//@SpringBootApplication
@EnableSwagger2
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class MTonAirServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MTonAirServerApplication.class, args);
    }



}
