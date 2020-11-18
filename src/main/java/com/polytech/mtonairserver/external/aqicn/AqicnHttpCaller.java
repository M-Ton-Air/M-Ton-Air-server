package com.polytech.mtonairserver.external.aqicn;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
                           /*@Value(value = "/?token=582a979a038e4dd717de2788124fd200620e1e3b") String token*/
                           @Value(value = "/?token=c35e2c9732b063aedfdde529afc64860398e565d") String token)
    {
        super(domain, token);
    }

    @Override
    public ResponseEntity<String> callExternalApi(String endpoint) throws RequestErrorException, InvalidTokenException, UnknownStationException {
        RestTemplate restTemplate = new RestTemplate();
        String urlToCall = this.domain + endpoint + this.token;

        ResponseEntity<String> jsonResponse = restTemplate.getForEntity(urlToCall, String.class);

        if (jsonResponse.getBody().contains("Unknown station")) {
            throw new UnknownStationException("The station name is unknown.", AqicnHttpCaller.class);
        }
        else if (jsonResponse.getBody().contains("Invalid key")) {
            throw new InvalidTokenException("The API AQICN token is invalid.", AqicnHttpCaller.class);
        }

        return jsonResponse;

    }




}