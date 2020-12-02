package com.polytech.mtonairserver.controller;

import com.polytech.mtonairserver.config.SwaggerConfig;
import com.polytech.mtonairserver.model.entities.DailyAqicnDataEntity;
import com.polytech.mtonairserver.model.entities.ForecastEntity;
import com.polytech.mtonairserver.model.responses.ApiErrorResponse;
import com.polytech.mtonairserver.model.responses.ApiResponse;
import com.polytech.mtonairserver.model.responses.ApiSuccessResponse;
import com.polytech.mtonairserver.service.implementation.DailyAqicnDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Configuration
@EnableScheduling
@RestController
@Api(tags = SwaggerConfig.DAILY_AQICN_DATA_NAME_TAG)
@RequestMapping("/aqicn-data")
public class DailyAqicnDataController {

    private DailyAqicnDataService dailyAqicnDataService;

    @Autowired
    public DailyAqicnDataController(DailyAqicnDataService dailyAqicnDataService) {
        this.dailyAqicnDataService = dailyAqicnDataService;
    }


    @ApiOperation(value = "Gets the AQICN data list.", notes = "Gets all the available AQICN data" +
            "stored in the M-Ton-Air database.")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<DailyAqicnDataEntity> listOfAqicnData() {
        return this.dailyAqicnDataService.listOfAqicnData();
    }


    @ApiOperation(value = "Gets the AQICN forecast data list.", notes = "Gets all the available AQICN forecast data" +
            "stored in the M-Ton-Air database.")
    @RequestMapping(value = "/forecast", method = RequestMethod.GET)
    @ResponseBody
    public List<ForecastEntity> listOfAqicnForecastData() {
        return this.dailyAqicnDataService.listOfAqicnForecastData();
    }


    //  A activer au besoin
    //@Scheduled(fixedDelay=86400000) // method starting every 24 hours
    @ApiOperation(value = "Save the AQICN data into the database.", notes = "Save all the available AQICN data" +
            "into the M-Ton-Air database.")
    @RequestMapping(value = "/save-data", method = RequestMethod.POST)
    public ApiResponse fillOutDailyAqicnData() {
        try {
            this.dailyAqicnDataService.fillOutDailyAqicnData();
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Could not fillOutDailyAqicnData ", e);
        }

        return new ApiSuccessResponse(HttpStatus.OK,
                "SAVE OK ");
    }

    @ApiOperation(value = "Gets the AQICN data according to the station id.", notes = "Retrieves all data of a station according to its id")
    @RequestMapping(value = "/{idStation}", method = RequestMethod.GET)
    public List<DailyAqicnDataEntity> getAllAqicnDatasOfAStation(
            @ApiParam(name = "idStation", value = "The station id", required = true)
            @PathVariable int idStation) {
        return this.dailyAqicnDataService.getAqicnDatasByIdStation(idStation);
    }

    @ApiOperation(value = "Gets the AQICN forecast data according to the station id.", notes = "Retrieves aqcin forecast data for a given station id")
    @RequestMapping(value = "/forecast/{idStation}", method = RequestMethod.GET)
    public List<ForecastEntity> getAqicnForecastDatasByIdStation(
            @ApiParam(name = "idStation", value = "The station id", required = true)
            @PathVariable int idStation) {
        return this.dailyAqicnDataService.getAqicnForecastDatasByIdStation(idStation);
    }

    @ApiOperation(value = "Gets the AQICN forecast data according to many ids.", notes = "Retrieves all forecast data of a station according to its id")
    @RequestMapping(value = "/by-ids", method = RequestMethod.GET)
    public List<DailyAqicnDataEntity> getAqicnDataByManyIdStation(
            @ApiParam(name = "idStation", value = "The stations ids", required = true)
            @RequestBody int[] stationIds) {
        return this.dailyAqicnDataService.getaqicnDataByManyIdStations(stationIds);
    }

    @ApiOperation(value = "Gets the AQICN forecast data according to the station id and the measure name.", notes = "Retrieves all aqicn data of a station according to its id and measure name")
    @RequestMapping(value = "/forecast/{idStation}/{measureName}", method = RequestMethod.GET)
    public List<ForecastEntity> getAqicnForecastDatasByIdStationAndMeasureName(
            @ApiParam(name = "idStation", value = "The station id", required = true)
            @PathVariable int idStation,
            @ApiParam(name = "measureName", value = "The measure name", required = true)
            @PathVariable String measureName) {
        return this.dailyAqicnDataService.getAqicnForecastDatasByIdStationAndMeasureName(idStation, measureName);
    }

}
