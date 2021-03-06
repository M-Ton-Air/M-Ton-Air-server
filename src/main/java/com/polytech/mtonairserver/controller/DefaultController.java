package com.polytech.mtonairserver.controller;

import com.polytech.mtonairserver.config.SwaggerConfig;
import com.polytech.mtonairserver.model.responses.ApiErrorResponse;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

@RestController
@Api(tags = SwaggerConfig.DEFAULT_TAG)
public class DefaultController implements org.springframework.boot.web.servlet.error.ErrorController
{
    private static final String PATH = "/error";

    private ServletContext servletContext;

    @Autowired
    public DefaultController(ServletContext servlet)
    {
        this.servletContext = servlet;
    }

    @RequestMapping(value = PATH)
    public ResponseEntity<ApiErrorResponse> error()
    {
        return new ResponseEntity<ApiErrorResponse>
        (
                new ApiErrorResponse(HttpStatus.BAD_REQUEST, "Given request could not be interpreted.", null),
                HttpStatus.BAD_REQUEST
        );
    }

    @Override
    public String getErrorPath()
    {
        return PATH;
    }

    @RequestMapping(value = "/")
    public void redirect(HttpServletResponse resp) throws IOException
    {
        resp.sendRedirect(servletContext.getContextPath() +"/swagger-ui.html");
    }
}
