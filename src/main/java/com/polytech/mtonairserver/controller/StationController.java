package com.polytech.mtonairserver.controller;

import com.polytech.mtonairserver.model.entities.StationEntity;
import com.polytech.mtonairserver.service.implementation.StationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/stations")
public class StationController {

    private StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @RequestMapping(value = "/stationsName", method = RequestMethod.GET)
    public List<StationEntity> getAllStationsName() throws IOException {
        return this.stationService.getAllStationsName();
    }

}