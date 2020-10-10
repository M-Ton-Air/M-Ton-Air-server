package com.polytech.mtonairserver;

import com.polytech.mtonairserver.MTonAirServerApplication;
import com.polytech.mtonairserver.customexceptions.ExceptionLogger;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MTonAirServerApplication.class);
    }

}
