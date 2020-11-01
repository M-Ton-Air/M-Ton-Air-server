package com.polytech.mtonairserver.controller;

import com.polytech.mtonairserver.config.SwaggerConfig;
import com.polytech.mtonairserver.customexceptions.requestaqicnexception.InvalidTokenException;
import com.polytech.mtonairserver.customexceptions.requestaqicnexception.UnknownStationException;
import com.polytech.mtonairserver.model.responses.ApiErrorResponse;
import com.polytech.mtonairserver.service.implementation.AqicnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;

/**
 * AQICN Controller
 */
@RestController
@Api(tags = SwaggerConfig.AQICN_NAME_TAG)
@RequestMapping("/aqicn")
public class AqicnController {

    private AqicnService aqicnService;

    @Autowired
    public AqicnController(AqicnService _aqicnService) {
        this.aqicnService = _aqicnService;
    }

    /**
     * Retrieve air quality data about a city.
     * @param stationBase station base we want to know about.
     * @param request request information.
     * @return a json file containing air quality information about the chosen city.
     * @throws UnknownStationException
     * @throws InvalidTokenException
     */
    @ApiOperation(value = "Get air quality information about a station", notes = "gets all the" +
            "air quality information about a station thanks to AQICN API.")
    @RequestMapping(value = "/{stationBase}/**", method= RequestMethod.GET)
    public ResponseEntity<String> requestAqicn(
            @ApiParam(name = "stationBase", value = "The station base name", required = true)
            @PathVariable String stationBase, HttpServletRequest request) throws UnknownStationException, InvalidTokenException {

        return this.aqicnService.requestAqicn(stationBase, request);
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
