package com.polytech.mtonairserver.controller;

import com.polytech.mtonairserver.customexceptions.datareader.NoProperLocationFoundException;
import com.polytech.mtonairserver.customexceptions.datareader.UnsupportedFindOperationOnLocationException;
import com.polytech.mtonairserver.customexceptions.stations.StationsAlreadyInitializedException;
import com.polytech.mtonairserver.model.entities.StationEntity;
import com.polytech.mtonairserver.model.responses.ApiErrorResponse;
import com.polytech.mtonairserver.model.responses.ApiSuccessResponse;
import com.polytech.mtonairserver.service.implementation.StationService;
import com.polytech.mtonairserver.stationshandling.io.DataReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/stations")
public class StationController {

    private final StationService stationService;


    @Autowired
    public StationController(StationService stationService, DataReader loader) {
        this.stationService = stationService;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<List<StationEntity>> getAllStationsName() throws IOException, NoProperLocationFoundException, UnsupportedFindOperationOnLocationException
    {
        return new ResponseEntity<List<StationEntity>>(this.stationService.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/create-stations", method = RequestMethod.PUT)
    public ResponseEntity insert() throws IOException
    {
        // todo : for each url of each record of the database : retrieve the geo (latitude / longitude) and store it.
        // todo : add a longitude / latitude field for the station (database)


        try
        {
            this.stationService.saveAllStationsToDatabaseFromFiles();
        }
        catch (StationsAlreadyInitializedException e)
        {
            e.printStackTrace();
            return new ResponseEntity<ApiErrorResponse>
            (
                    new ApiErrorResponse(HttpStatus.CONFLICT, "Stations are already initialized", e),
                    HttpStatus.CONFLICT
            );
        }
        catch (NoProperLocationFoundException | UnsupportedFindOperationOnLocationException e)
        {
            e.printStackTrace();
            return new ResponseEntity<ApiErrorResponse>
            (
                    new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong on server side while initializing the stations", e),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<ApiErrorResponse>
            (
                    new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unknown error occured", e),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
        return new ResponseEntity<ApiSuccessResponse>
        (
                new ApiSuccessResponse(HttpStatus.OK, "Stations were retrieved from the server files and saved into the database."),
                HttpStatus.OK
        );
    }


    //todo : delete all
}