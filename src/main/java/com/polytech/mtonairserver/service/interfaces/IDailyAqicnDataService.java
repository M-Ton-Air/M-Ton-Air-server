package com.polytech.mtonairserver.service.interfaces;

import com.polytech.mtonairserver.customexceptions.requestaqicnexception.InvalidTokenException;
import com.polytech.mtonairserver.customexceptions.requestaqicnexception.UnknownStationException;
import com.polytech.mtonairserver.model.entities.DailyAqicnDataEntity;
import com.polytech.mtonairserver.model.entities.ForecastEntity;
import io.swagger.annotations.ApiResponse;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface IDailyAqicnDataService {

    List<DailyAqicnDataEntity> listOfAqicnData();

    List<ForecastEntity> listOfAqicnForecastData();

    void fillOutDailyAqicnData() throws UnknownStationException, InvalidTokenException, ParseException, IOException, ExecutionException, InterruptedException;

    List<DailyAqicnDataEntity> getAqicnDatasByIdStation(int idStation);

    List<ForecastEntity> getAqicnForecastDatasByIdStation(int idStation);

    List<ForecastEntity> getAqicnForecastDatasByIdStationAndMeasureName(int idStation, String measureName);


}
