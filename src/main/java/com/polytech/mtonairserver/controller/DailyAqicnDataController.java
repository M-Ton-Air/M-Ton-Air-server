package com.polytech.mtonairserver.controller;

import com.polytech.mtonairserver.config.SwaggerConfig;
import com.polytech.mtonairserver.model.entities.DailyAqicnDataEntity;
import com.polytech.mtonairserver.model.entities.ForecastEntity;
import com.polytech.mtonairserver.model.entities.UserEntity;
import com.polytech.mtonairserver.model.responses.ApiErrorResponse;
import com.polytech.mtonairserver.model.responses.ApiResponse;
import com.polytech.mtonairserver.model.responses.ApiSuccessResponse;
import com.polytech.mtonairserver.service.implementation.DailyAqicnDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = SwaggerConfig.DAILY_AQICN_DATA_NAME_TAG)
@RequestMapping("/aqicn-data")
public class DailyAqicnDataController {

    private DailyAqicnDataService dailyAqicnDataService;

    @Autowired
    public DailyAqicnDataController(DailyAqicnDataService dailyAqicnDataService) {
        this.dailyAqicnDataService = dailyAqicnDataService;
    }


    @ApiOperation(value = "Get the AQICN datas list", notes = "gets all the available AQICN data" +
            "stored in the M-Ton-Air database.")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<DailyAqicnDataEntity> listOfAqicnData() {
        return this.dailyAqicnDataService.listOfAqicnData();
    }


    @ApiOperation(value = "Get the AQICN forecast datas list", notes = "gets all the available AQICN forecast data" +
            "stored in the M-Ton-Air database.")
    @RequestMapping(value = "/forecast", method = RequestMethod.GET)
    @ResponseBody
    public List<ForecastEntity> listOfAqicnForecastData() {
        return this.dailyAqicnDataService.listOfAqicnForecastData();
    }


    @ApiOperation(value = "Save the AQICN datas into the database", notes = "Save all the available AQICN data" +
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

    @ApiOperation(value = "Gets the AQICN datas with the id station", notes = "Retrieves all datas of a station according to his id")
    @RequestMapping(value = "/{idStation}", method = RequestMethod.GET)
    public List<DailyAqicnDataEntity> getAllAqicnDatasOfAStation(
            @ApiParam(name = "idStation", value = "The station id", required = true)
            @PathVariable int idStation) {
        return this.dailyAqicnDataService.getAqicnDatasByIdStation(idStation);
    }

    @ApiOperation(value = "Gets the AQICN forecast datas with the id station", notes = "Retrieves all forecast datas of a station according to his id")
    @RequestMapping(value = "/forecast/{idStation}", method = RequestMethod.GET)
    public List<ForecastEntity> getAqicnForecastDatasByIdStation(
            @ApiParam(name = "idStation", value = "The station id", required = true)
            @PathVariable int idStation) {
        return this.dailyAqicnDataService.getAqicnForecastDatasByIdStation(idStation);
    }

    @ApiOperation(value = "Gets the AQICN forecast datas with the id station and the measure name", notes = "Retrieves all forecast datas of a station according to his id and the measure name")
    @RequestMapping(value = "/forecast/{idStation}/{measureName}", method = RequestMethod.GET)
    public List<ForecastEntity> getAqicnForecastDatasByIdStationAndMeasureName(
            @ApiParam(name = "idStation", value = "The station id", required = true)
            @PathVariable int idStation,
            @ApiParam(name = "measureName", value = "The measure name", required = true)
            @PathVariable String measureName) {
        return this.dailyAqicnDataService.getAqicnForecastDatasByIdStationAndMeasureName(idStation, measureName);
    }

}
