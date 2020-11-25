package com.polytech.mtonairserver.controller;

import com.polytech.mtonairserver.config.SwaggerConfig;
import com.polytech.mtonairserver.customexceptions.requestaqicnexception.CoordinatesRetrievalException;
import com.polytech.mtonairserver.customexceptions.requestaqicnexception.InvalidTokenException;
import com.polytech.mtonairserver.customexceptions.requestaqicnexception.RequestErrorException;
import com.polytech.mtonairserver.customexceptions.requestaqicnexception.UnknownStationException;
import com.polytech.mtonairserver.model.responses.ApiResponse;
import com.polytech.mtonairserver.model.responses.ApiSuccessResponse;
import com.polytech.mtonairserver.service.implementation.GeoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * This controller is disabled.
 * Its purpose was to query all the AQICN endpoints and to retrieve ALL the stations coordinates, and to save
 * them to a json file locally.
 * That json file is available under the resources folder.
 *
 * That endpoint must be reactivated ONLY IF :
 *
 * - You changed the GeoService so that it saves the coordinates json file well.
 * - Stations.html was updated and so were AQICN stations, so you need new coordinates.
 *
 * Otherwise, please don't re-enable this controller because it must stay unavailable ; querying all the
 * aqicn endpoints and saving it into a json file takes approximatively 50 minutes, even with multi-threading.
 * That method can be optimized.
 *
 */
@RestController
@RequestMapping("/geo/stations")
@ConditionalOnExpression("${geocontroller.enabled:false}")
@Api(tags = SwaggerConfig.GEO_NAME_TAG)
public class GeoController
{
    private GeoService geoService;

    @Autowired
    public GeoController(GeoService geoService)
    {
        this.geoService = geoService;
    }

    @ApiOperation(value = "Get the coordinates of all stations on the AQICN API", notes = "Allows to request the AQICN API and to save the stations coordinates in a json file.")
    @RequestMapping(value = "/save-all", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse> saveAllStationsToJsonFile() throws IOException, InterruptedException, ExecutionException, InvalidTokenException, CoordinatesRetrievalException, RequestErrorException, UnknownStationException
    {
        this.geoService.saveAllAqicnStationsCoordinatesToJsonResourceFile();
        return new ResponseEntity<>
        (
            new ApiSuccessResponse(HttpStatus.OK, "Data were saved to the json file on server side."),
            HttpStatus.OK
        );
    }
}
