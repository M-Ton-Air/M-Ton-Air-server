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

import static com.polytech.mtonairserver.customexceptions.ControllerExceptionBuilder.buildErrorResponseAndPrintStackTrace;

/**
 * AQICN Controller
 */
@RestController
@Api(tags = SwaggerConfig.AQICN_NAME_TAG)
@RequestMapping("/aqicn")
public class AqicnController {

    private AqicnService aqicnService;

    private final String partOfUrlToRemove = "/api/v1/aqicn/";

    @Autowired
    public AqicnController(AqicnService _aqicnService) {
        this.aqicnService = _aqicnService;
    }

    /**
     * Retrieve air quality data about a city.
     * @param request request information.
     * @return a json file containing air quality information about the chosen city.
     * @throws UnknownStationException
     * @throws InvalidTokenException
     */
    @ApiOperation(value = "Get air quality information about a station", notes = "gets all the" +
            "air quality information about a station thanks to AQICN API.")
    @RequestMapping(value = "/**", method= RequestMethod.GET)
    public ResponseEntity<String> requestAqicn(HttpServletRequest request) throws UnknownStationException, InvalidTokenException, Exception {
        String endpoint = request.getRequestURI().replace(this.partOfUrlToRemove, "");
        return this.aqicnService.requestAqicn(endpoint);
    }


    /* ############################################################## EXCEPTION HANDLERS ############################################################## */

    @ExceptionHandler(UnknownStationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorResponse>  unknownStationResponse(UnknownStationException ex)
    {
        return buildErrorResponseAndPrintStackTrace(HttpStatus.BAD_REQUEST, "The station name does not exist.", ex);
    }

    @ExceptionHandler(InvalidTokenException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorResponse> invalidTokenResponse(InvalidTokenException ex)
    {
        return buildErrorResponseAndPrintStackTrace(HttpStatus.BAD_REQUEST, "The API AQICN token is invalid.", ex);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorResponse> unknownException(Exception ex)
    {
        return buildErrorResponseAndPrintStackTrace(HttpStatus.INTERNAL_SERVER_ERROR, "Unkown error", ex);
    }
}
