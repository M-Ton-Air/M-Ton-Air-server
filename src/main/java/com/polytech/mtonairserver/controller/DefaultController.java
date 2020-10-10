package com.polytech.mtonairserver.controller;

import com.polytech.mtonairserver.model.responses.ApiErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

@RestController
public class DefaultController implements org.springframework.boot.web.servlet.error.ErrorController
{
    private static final String PATH = "/error";

    @Autowired
    private ServletContext servletContext;

    @RequestMapping(value = PATH)
    public ApiErrorResponse error()
    {
        return new ApiErrorResponse(HttpStatus.BAD_REQUEST, "Given request could not be interprated.", null);
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
