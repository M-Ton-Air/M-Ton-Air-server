package com.polytech.mtonairserver.service.implementation;

import com.polytech.mtonairserver.controller.AqicnController;
import com.polytech.mtonairserver.customexceptions.requestaqicnexception.InvalidTokenException;
import com.polytech.mtonairserver.customexceptions.requestaqicnexception.UnknownStationException;
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
    private static String hostLinkJsonAPI = "https://api.waqi.info" ;
    private static String path = "/feed/";
    //the API AQICN token
    private static String token = "/?token=582a979a038e4dd717de2788124fd200620e1e3b";
    //the part of the URL to remove.
    private static String partOfUrlToRemove = "/api/v1/aqicn/";

    public static String getHostLinkJsonAPI() {
        return hostLinkJsonAPI;
    }

    /**
     * Retrieve air quality data about a city.
     * @param stationBase station base we want to know about.
     * @param request request information.
     * @return a json file containing air quality information about the chosen city.
     * @throws UnknownStationException
     * @throws InvalidTokenException
     */
    public ResponseEntity<String> requestAqicn(String stationBase, HttpServletRequest request) throws UnknownStationException, InvalidTokenException {
        // full station name. Example : california/san-diego/el-cajon-redwood-avenue
        String stationName = request.getRequestURI().replace(partOfUrlToRemove, "");

        RestTemplate restTemplate = new RestTemplate();
        //ResponseEntity<String> stringResponseEntity = restTemplate.getForEntity(hostLinkJsonAPI + path + stationName + token, String.class);
        ResponseEntity<String> stringResponseEntity = restTemplate.getForEntity(hostLinkJsonAPI + path + stationBase + token, String.class);


        // if the station is unknown ...
        if (stringResponseEntity.getBody().contains("Unknown station")) {
            throw new UnknownStationException("The station name is unknown.", AqicnController.class);
        }
        // if the API AQICN token is invalid
        else if (stringResponseEntity.getBody().contains("Invalid key")) {
            throw new InvalidTokenException("The API AQICN token is invalid.", AqicnController.class);
        }

        return stringResponseEntity;
    }

}
