package com.polytech.mtonairserver.controller;

import com.polytech.mtonairserver.config.SwaggerConfig;
import com.polytech.mtonairserver.customexceptions.ControllerExceptionBuilder;
import com.polytech.mtonairserver.customexceptions.datareader.NoProperLocationFoundException;
import com.polytech.mtonairserver.customexceptions.datareader.UnsupportedFindOperationOnLocationException;
import com.polytech.mtonairserver.customexceptions.stations.StationsAlreadyInitializedException;
import com.polytech.mtonairserver.model.entities.StationEntity;
import com.polytech.mtonairserver.model.responses.ApiErrorResponse;
import com.polytech.mtonairserver.model.responses.ApiSuccessResponse;
import com.polytech.mtonairserver.service.implementation.StationService;
import com.polytech.mtonairserver.stationshandling.io.DataReader;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static com.polytech.mtonairserver.customexceptions.ControllerExceptionBuilder.buildErrorResponseAndPrintStackTrace;

@RestController
@RequestMapping("/stations")
@Api(tags = SwaggerConfig.STATIONS_NAME_TAG)
public class StationController {

    private final StationService stationService;

    @Autowired
    public StationController(StationService stationService, DataReader loader) {
        this.stationService = stationService;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ApiOperation(value = "Stations list", notes = "Retrieves all the AQICN stations.")
    public ResponseEntity<List<StationEntity>> getAllStationsName() throws IOException, NoProperLocationFoundException, UnsupportedFindOperationOnLocationException
    {
        return new ResponseEntity<List<StationEntity>>(this.stationService.findAll(), HttpStatus.OK);
    }

    //todo : find by id, by name, by subdivision, etc.

    /**
     * This methods loads up all the stations into memory thanks to the "stations.html" file. Then, it initializes
     * a list of StationEntity, to save them up into the mtonair database.
     * @return
     * @throws IOException
     * @throws StationsAlreadyInitializedException the current method can be started only if there are no more than one records in
     * the station table. So, if this method has already been executed or if there's a record in the station table, it won't proceed.
     * @throws UnsupportedFindOperationOnLocationException This method is raised if there are problems with the DataReader.
     * @throws NoProperLocationFoundException Same than for UnsupportedFindOperation.
     */
    @ApiOperation(value = "Stations creation", notes = "Creates and inserts all the aqicn stations into the database.")
    @RequestMapping(value = "/create-stations", method = RequestMethod.PUT)
    public ResponseEntity insertAllStations() throws IOException, StationsAlreadyInitializedException, UnsupportedFindOperationOnLocationException, NoProperLocationFoundException
    {
        // todo : for each url of each record of the database : retrieve the geo (latitude / longitude) and store it.
        // todo : add a longitude / latitude field for the station (database)

        this.stationService.saveAllStationsToDatabaseFromFiles();
        return new ResponseEntity<ApiSuccessResponse>
        (
                new ApiSuccessResponse(HttpStatus.OK, "Stations were retrieved from the server files and saved into the database."),
                HttpStatus.OK
        );
    }


    //todo : delete all



    /* ############################################################## EXCEPTION HANDLERS ############################################################## */


    @ExceptionHandler(NoProperLocationFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<ApiErrorResponse> insertAllStationsNoProperLocation(NoProperLocationFoundException e)
    {
        return buildErrorResponseAndPrintStackTrace(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong on server side while initializing the stations", e);
    }

    @ExceptionHandler(NoProperLocationFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<ApiErrorResponse> insertAllStationsUnsupportedFindOperation(UnsupportedFindOperationOnLocationException e)
    {
        return buildErrorResponseAndPrintStackTrace(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong on server side while initializing the stations", e);
    }

    @ExceptionHandler(StationsAlreadyInitializedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    private ResponseEntity<ApiErrorResponse> insertAllStationsStationsAlreadyInitialized(StationsAlreadyInitializedException e)
    {
        return buildErrorResponseAndPrintStackTrace(HttpStatus.CONFLICT, "Stations are already initialized", e);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<ApiErrorResponse> insertAllStationsUnkownError(Exception e)
    {
        return buildErrorResponseAndPrintStackTrace(HttpStatus.INTERNAL_SERVER_ERROR, "An unknown error occured", e);
    }
}