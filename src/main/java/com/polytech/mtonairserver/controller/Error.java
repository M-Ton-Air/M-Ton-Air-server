package com.polytech.mtonairserver.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;

@RestController
public class Error implements org.springframework.boot.web.servlet.error.ErrorController
{
    private static final String PATH = "/error";

    @RequestMapping(value = PATH)
    public String error()
    {
        return "Could not give a response to the current request";
    }

    @Override
    public String getErrorPath()
    {
        return PATH;
    }
}
