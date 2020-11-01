package com.polytech.mtonairserver.controller;

import com.polytech.mtonairserver.config.SwaggerConfig;
import com.polytech.mtonairserver.customexceptions.requestaqicnexception.InvalidTokenException;
import com.polytech.mtonairserver.customexceptions.requestaqicnexception.UnknownStationException;
import com.polytech.mtonairserver.model.responses.ApiErrorResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.InvalidKeyException;

@RestController
@Api(tags = SwaggerConfig.AQICN_NAME_TAG)
@RequestMapping("/aqicn")
public class AqicnController {


    private final static String host = "https://api.waqi.info" ;
    private final static String path = "/feed/";
    //the API AQICN token
    private final static String token = "/?token=582a979a038e4dd717de2788124fd200620e1e3b";
    //the part of the URL to remove.
    private final static String partOfUrlToRemove = "/api/v1/aqicn/";

    /**
     * Retrieve air quality data about a city.
     * @param stationBase station base we want to know about.
     * @param request request information.
     * @return a json file containing air quality information about the chosen city.
     * @throws IOException
     * @throws UnknownStationException
     */
    @ApiOperation(value = "Get air quality information about a station", notes = "gets all the" +
            "air quality information about a station thanks to AQICN API.")
    @RequestMapping(value = "/{stationBase}/**", method= RequestMethod.GET)
    public ResponseEntity<String> requestAqicn(
            @ApiParam(name = "stationBase", value = "The station base name", required = true)
            @PathVariable String stationBase, HttpServletRequest request) throws IOException, UnknownStationException, InvalidTokenException {

        // full station name. Example : california/san-diego/el-cajon-redwood-avenue
        String stationName = request.getRequestURI().replace(partOfUrlToRemove, "");

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> stringResponseEntity = restTemplate.getForEntity(host + path + stationName + token, String.class);

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


    /* ############################################################## EXCEPTION HANDLERS ############################################################## */

    /**
     * Custom Exception Handler for invalid emails.
     * @param ex an InvalidEmailException
     * @return an api error response describing what went wrong to the api user.
     */
    @ExceptionHandler(UnknownStationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse unknownStationResponse(UnknownStationException ex)
    {
        ex.setStackTrace(new StackTraceElement[]{ex.getStackTrace()[0]});
        return new ApiErrorResponse(HttpStatus.BAD_REQUEST, "The station name does not exist.", ex);
    }

    /**
     * Custom Exception Handler for invalid tokens.
     * @param ex an InvalidTokenException.
     * @return an api error response describing what went wrong to the api user.
     */
    @ExceptionHandler(InvalidTokenException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse invalidTokenResponse(InvalidTokenException ex)
    {
        ex.setStackTrace(new StackTraceElement[]{ex.getStackTrace()[0]});
        return new ApiErrorResponse(HttpStatus.BAD_REQUEST, "The API AQICN token is invalid.", ex);
    }
}
