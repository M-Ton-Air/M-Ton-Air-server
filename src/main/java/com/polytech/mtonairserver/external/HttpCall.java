package com.polytech.mtonairserver.external;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.polytech.mtonairserver.customexceptions.LoggableException;
import com.polytech.mtonairserver.customexceptions.requestaqicnexception.RequestErrorException;
import net.minidev.json.JSONObject;
import org.springframework.http.ResponseEntity;
import springfox.documentation.spring.web.json.Json;

public abstract class HttpCall
{
    protected String domain;
    protected String token;

    public abstract ResponseEntity<String> callExternalApi(String endpoint) throws LoggableException;

    public HttpCall(String domain, String token)
    {
        this.domain = domain;
        this.token = token;
    }
}
