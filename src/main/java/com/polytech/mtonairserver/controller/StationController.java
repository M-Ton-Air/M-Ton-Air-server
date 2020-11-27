package com.polytech.mtonairserver.controller;

import com.polytech.mtonairserver.config.SwaggerConfig;
import com.polytech.mtonairserver.customexceptions.datareader.NoProperLocationFoundException;
import com.polytech.mtonairserver.customexceptions.datareader.UnsupportedFindOperationOnLocationException;
import com.polytech.mtonairserver.customexceptions.stations.StationsAlreadyInitializedException;
import com.polytech.mtonairserver.model.entities.DailyAqicnDataEntity;
import com.polytech.mtonairserver.model.entities.StationEntity;
import com.polytech.mtonairserver.model.responses.ApiErrorResponse;
import com.polytech.mtonairserver.model.responses.ApiSuccessResponse;
import com.polytech.mtonairserver.service.implementation.StationService;
import com.polytech.mtonairserver.stationshandling.io.StationsDataReader;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.polytech.mtonairserver.customexceptions.ControllerExceptionBuilder.buildErrorResponseAndPrintStackTrace;

@RestController
@RequestMapping("/stations")
@Api(tags = SwaggerConfig.STATIONS_NAME_TAG)
public class StationController {

    private final StationService stationService;

    @Autowired
    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ApiOperation(value = "Stations list", notes = "Retrieves all the AQICN stations.")
    public ResponseEntity<List<StationEntity>> getAllStationsName()
    {
        return new ResponseEntity<List<StationEntity>>(this.stationService.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{idStation}", method = RequestMethod.GET)
    @ApiOperation(value = "Get a specific station with his id", notes = "Retrieves a specific station according to his id")
    public ResponseEntity<StationEntity> getAStationById(
            @ApiParam(name = "idStation", value = "The station id", required = true)
            @PathVariable int idStation) {
        return new ResponseEntity<StationEntity>(this.stationService.findStationById(idStation), HttpStatus.OK);
    }

    @RequestMapping(value = "/by-country/{country}", method = RequestMethod.GET)
    @ApiOperation(value = "Stations list by country", notes = "Retrieves all the AQICN stations by a country.")
    public ResponseEntity<List<StationEntity>> getStationsByCountry(
            @ApiParam(name = "country", value = "The country name", required = true)
            @PathVariable String country
    )
    {
        return new ResponseEntity<List<StationEntity>>(this.stationService.findAllByCountry(country), HttpStatus.OK);
    }

    @RequestMapping(value = "/by-name/{stationName}", method = RequestMethod.GET)
    @ApiOperation(value = "Stations list by name", notes = "Retrieves  AQICN stations by a station name.")
    public ResponseEntity<List<StationEntity>> getStationsByStationName(
            @ApiParam(name = "stationName", value = "The station name", required = true)
            @PathVariable String stationName
    )
    {
        return new ResponseEntity<List<StationEntity>>(this.stationService.findAllByStationName(stationName), HttpStatus.OK);
    }

    @RequestMapping(value = "/by-iso2/{iso2}", method = RequestMethod.GET)
    @ApiOperation(value = "Stations list by iso2", notes = "Retrieves all the AQICN stations by iso2.")
    public ResponseEntity<List<StationEntity>> getStationsByIso2(
            @ApiParam(name = "iso2", value = "The iso2", required = true)
            @PathVariable String iso2
    )
    {
        return new ResponseEntity<List<StationEntity>>(this.stationService.findAllByIso2(iso2), HttpStatus.OK);
    }

    @RequestMapping(value = "/by-subdivision/{subdivision}", method = RequestMethod.GET)
    @ApiOperation(value = "Stations list by the subdivision", notes = "Retrieves all the AQICN stations by the subdivision1, " +
            "the subdivision2 or the subdivision3.")
    public ResponseEntity<List<StationEntity>> getStationsBySubdivision(
            @ApiParam(name = "subdivision", value = "The subdivision", required = true)
            @PathVariable String subdivision
    )
    {
        return new ResponseEntity<List<StationEntity>>(this.stationService.findAllBySubdivision(subdivision), HttpStatus.OK);
    }

    @RequestMapping(value = "/by-all/{any}/{numberOfStations}", method = RequestMethod.GET)
    @ApiOperation(value = "Stations list by the station name, the country or the subdivision", notes = "Retrieves the AQICN stations by the station name, " +
            "the country the subdivision1, the subdivision2 or the subdivision3.")
    public ResponseEntity<List<StationEntity>> getStationsByStationNameAndCountryAndSubdivision(
            @ApiParam(name = "any", value = "It can be the station name, the country or the subdivision", required = true)
            @PathVariable String any,
            @ApiParam(name = "numberOfStations", value = "Number of stations we want to get", required = true)
            @PathVariable int numberOfStations) {
        return new ResponseEntity<List<StationEntity>>(this.stationService.findByStationNameAndCountryAndSubdivision(any, numberOfStations), HttpStatus.OK);
    }

    @RequestMapping(value = "/delete-all", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete all stations", notes = "Delete all stations from the mtonairserver database ")
    public ResponseEntity deleteAllStations()
    {
        this.stationService.deleteAll();
        return new ResponseEntity<ApiSuccessResponse>
                (
                        new ApiSuccessResponse(HttpStatus.OK, "Stations were removed from the database."),
                        HttpStatus.OK
                );
    }

    /**
     * This methods loads up all the stations into memory thanks to the "stations.html" file (and the stations_geo.json file).
     * Then, it initializes a list of StationEntity, to save them up into the mtonair database.
     *
     * Please, instead of using this endpoint, use the appropriate SQL script "insert-stations".
     * That method takes about 1min30 to complete.
     *
     * @return
     * @throws IOException
     * @throws StationsAlreadyInitializedException the current method can be started only if there are no more than one records in
     * the station table. So, if this method has already been executed or if there's a record in the station table, it won't proceed.
     * @throws UnsupportedFindOperationOnLocationException This method is raised if there are problems with the DataReader.
     * @throws NoProperLocationFoundException Same than for UnsupportedFindOperation.
     */
    @ApiOperation(value = "Stations creation", notes = "Creates and inserts all the aqicn stations into the database.")
    @RequestMapping(value = "/create-stations", method = RequestMethod.PUT)
    public ResponseEntity insertAllStations() throws IOException, StationsAlreadyInitializedException, UnsupportedFindOperationOnLocationException, NoProperLocationFoundException, ExecutionException, InterruptedException
    {
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

    @ExceptionHandler(ExecutionException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<ApiErrorResponse> insertAllStationsExecutionError(ExecutionException e)
    {
        return buildErrorResponseAndPrintStackTrace(HttpStatus.INTERNAL_SERVER_ERROR, "A threading error occured", e);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<ApiErrorResponse> insertAllStationsInterruptedError(InterruptedException e)
    {
        return buildErrorResponseAndPrintStackTrace(HttpStatus.INTERNAL_SERVER_ERROR, "An threading error occured", e);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<ApiErrorResponse> insertAllStationsUnkownError(Exception e)
    {
        return buildErrorResponseAndPrintStackTrace(HttpStatus.INTERNAL_SERVER_ERROR, "An unknown error occured", e);
    }
}