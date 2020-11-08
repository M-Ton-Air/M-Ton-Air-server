package com.polytech.mtonairserver.external.aqicn;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.polytech.mtonairserver.customexceptions.miscellaneous.EmptyBodyJsonResponseException;
import com.polytech.mtonairserver.customexceptions.requestaqicnexception.InvalidTokenException;
import com.polytech.mtonairserver.customexceptions.requestaqicnexception.RequestErrorException;
import com.polytech.mtonairserver.customexceptions.requestaqicnexception.UnknownStationException;
import com.polytech.mtonairserver.external.HttpCall;
import com.polytech.mtonairserver.service.implementation.AqicnService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AqicnHttpCaller extends HttpCall
{
    private static String partOfUrlToRemove = "/api/v1/aqicn/";

    public AqicnHttpCaller(@Value(value = "https://api.waqi.info/feed/")String domain,
                           @Value(value = "?token=582a979a038e4dd717de2788124fd200620e1e3b") String token)
    {
        super(domain, token);
    }

    @Override
    public JsonObject callExternalApi(String endpoint) throws RequestErrorException, InvalidTokenException, UnknownStationException, EmptyBodyJsonResponseException
    {
        RestTemplate restTemplate = new RestTemplate();
        String urlToCall = this.domain + endpoint + this.token;

        ResponseEntity<String> jsonResponseAsString = restTemplate.getForEntity(urlToCall, String.class);

        if(jsonResponseAsString.getStatusCode().equals(HttpStatus.OK))
        {
            String jsonString = jsonResponseAsString.getBody();
            if(jsonString == null)
            {
                throw new EmptyBodyJsonResponseException("Http response body was null / empty.", AqicnHttpCaller.class);
            }
            JsonElement jsonElement = this.parser.parse(jsonString);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonElement status = jsonObject.get("status");

            if(status.getAsString().equals("ok"))
            {
                   return jsonObject;
            }
            else if(status.getAsString().equals("error"))
            {
                handleError(endpoint, jsonResponseAsString, jsonObject);
            }
            else
            {
                throw new RequestErrorException(jsonResponseAsString.getStatusCode() + " : " + jsonResponseAsString.getBody(), AqicnHttpCaller.class);
            }
        }

         throw new RequestErrorException(jsonResponseAsString.getStatusCode() + " : " + jsonResponseAsString.getBody(), AqicnHttpCaller.class);
    }

    private void handleError(String endpoint, ResponseEntity<String> jsonResponseAsString, JsonObject jsonObject) throws UnknownStationException, InvalidTokenException, RequestErrorException {
        JsonElement error = jsonObject.get("data");
        if(error.getAsString().equals("Unknown station"))
        {
            throw new UnknownStationException("The station name is unknown : " + endpoint, AqicnHttpCaller.class);
        }
        else if(error.getAsString().equals("Invalid key"))
        {
            throw new InvalidTokenException("The API AQICN token is invalid.", AqicnHttpCaller.class);
        }
        else
        {
            throw new RequestErrorException(jsonResponseAsString.getStatusCode() + " : " + jsonResponseAsString.getBody(), AqicnHttpCaller.class);
        }
    }


}
