package com.polytech.mtonairserver.service.implementation;

import com.polytech.mtonairserver.controller.AqicnController;
import com.polytech.mtonairserver.customexceptions.miscellaneous.EmptyBodyJsonResponseException;
import com.polytech.mtonairserver.customexceptions.requestaqicnexception.InvalidTokenException;
import com.polytech.mtonairserver.customexceptions.requestaqicnexception.RequestErrorException;
import com.polytech.mtonairserver.customexceptions.requestaqicnexception.UnknownStationException;
import com.polytech.mtonairserver.external.aqicn.AqicnHttpCaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

/**
 * Service implementation for the AQICN service.
 */
@Service
public class AqicnService
{
    private AqicnHttpCaller aqicnCaller;

    @Autowired
    public AqicnService(AqicnHttpCaller aqicnHttpCaller)
    {
        this.aqicnCaller = aqicnHttpCaller;
    }

    /**
     * Retrieve air quality data about a city.
     * @param stationEndpoint station base we want to know about.
     * @return a json file containing air quality information about the chosen city.
     * @throws UnknownStationException
     * @throws InvalidTokenException
     */
    public String requestAqicn(String stationEndpoint) throws UnknownStationException, InvalidTokenException, RequestErrorException, EmptyBodyJsonResponseException
    {
        return this.aqicnCaller.callExternalApi(stationEndpoint).getBody();
    }
}
