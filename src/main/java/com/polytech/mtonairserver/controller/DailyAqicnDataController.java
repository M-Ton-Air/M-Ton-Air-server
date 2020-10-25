package com.polytech.mtonairserver.controller;

import com.polytech.mtonairserver.config.SwaggerConfig;
import com.polytech.mtonairserver.model.entities.DailyAqicnDataEntity;
import com.polytech.mtonairserver.service.implementation.DailyAqicnDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = SwaggerConfig.DAILY_AQICN_DATA_NAME_TAG)
@RequestMapping("/aqicnData")
public class DailyAqicnDataController {

    private DailyAqicnDataService dailyAqicnDataService;

    @Autowired
    public DailyAqicnDataController(DailyAqicnDataService dailyAqicnDataService) {
        this.dailyAqicnDataService = dailyAqicnDataService;
    }

    @ApiOperation(value = "Get the AQICN datas list", notes = "gets all the available AQICN data" +
            "stored in the M-Ton-Air database.")
    @RequestMapping(method = RequestMethod.GET)
    public List<DailyAqicnDataEntity> listOfAqicnData() {
        return this.dailyAqicnDataService.findAll();
    }
}
