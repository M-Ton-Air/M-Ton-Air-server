package com.polytech.mtonairserver.service.interfaces;

import com.polytech.mtonairserver.customexceptions.requestaqicnexception.AqiNotFoundException;
import com.polytech.mtonairserver.customexceptions.requestaqicnexception.InvalidTokenException;
import com.polytech.mtonairserver.customexceptions.requestaqicnexception.UnknownStationException;
import com.polytech.mtonairserver.model.entities.DailyAqicnDataEntity;
import com.polytech.mtonairserver.model.entities.ForecastEntity;
import com.polytech.mtonairserver.model.ReponseObject.GlobalObject;
import io.swagger.annotations.ApiResponse;
import javafx.util.Pair;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface IDailyAqicnDataService {

    List<DailyAqicnDataEntity> listOfAqicnData();

    List<ForecastEntity> listOfAqicnForecastData();

    void fillOutDailyAqicnData() throws UnknownStationException, InvalidTokenException, ParseException, IOException, ExecutionException, InterruptedException, AqiNotFoundException;

    Pair<List<DailyAqicnDataEntity>, List<ForecastEntity>> fillInDailyAndForecastAqicnData() throws ParseException, AqiNotFoundException, ExecutionException, InterruptedException;

    List<DailyAqicnDataEntity> getAqicnDatasByIdStation(int idStation);

    List<ForecastEntity> getAqicnForecastDatasByIdStation(int idStation);

    List<ForecastEntity> getAqicnForecastDatasByIdStationAndMeasureName(int idStation, String measureName);

    GlobalObject deserializationJsonAqicnData(String jsonAqicnData);


}
