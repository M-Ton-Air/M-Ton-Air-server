package com.polytech.mtonairserver.service.implementation;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.polytech.mtonairserver.customexceptions.requestaqicnexception.AqiNotFoundException;
import com.polytech.mtonairserver.customexceptions.requestaqicnexception.InvalidTokenException;
import com.polytech.mtonairserver.customexceptions.requestaqicnexception.RequestErrorException;
import com.polytech.mtonairserver.customexceptions.requestaqicnexception.UnknownStationException;
import com.polytech.mtonairserver.external.aqicn.AqicnHttpCaller;
import com.polytech.mtonairserver.model.entities.*;
import com.polytech.mtonairserver.model.responseobject.data.forecast.ValuesMeasureDailyForecastData;
import com.polytech.mtonairserver.repository.DailyAqicnDataRepository;
import com.polytech.mtonairserver.repository.ForecastRepository;
import com.polytech.mtonairserver.repository.MeasureRepository;
import com.polytech.mtonairserver.repository.StationRepository;
import com.polytech.mtonairserver.service.interfaces.IDailyAqicnDataService;
import com.polytech.mtonairserver.utils.doubleextensions.DoubleUtils;
import javafx.util.Pair;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.polytech.mtonairserver.model.responseobject.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Service implementation for the daily AQICN data.
 */
@Service
public class  DailyAqicnDataService implements IDailyAqicnDataService {

    private DailyAqicnDataRepository dailyAqicnDataRepository;

    private StationRepository stationRepository;

    private MeasureRepository measureRepository;

    private ForecastRepository forecastRepository;

    private AqicnHttpCaller aqicnHttpCaller;

    private static final FastDateFormat dateTimeFormat = FastDateFormat.getInstance("yyyy-MM-dd hh:mm:ss");
    private static final FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd");

    @Autowired
    public DailyAqicnDataService(DailyAqicnDataRepository dailyAqicnDataRepository, StationRepository stationRepository, MeasureRepository measureRepository, ForecastRepository forecastRepository, AqicnHttpCaller aqicnHttpCaller) {
        this.dailyAqicnDataRepository = dailyAqicnDataRepository;
        this.stationRepository = stationRepository;
        this.measureRepository = measureRepository;
        this.forecastRepository = forecastRepository;
        this.aqicnHttpCaller = aqicnHttpCaller;
    }

    /**
     * Gets all the AQICN data
     * @return all the AQICN data
     */
    @Override
    @Cacheable(value = "aqicndata")
    public List<DailyAqicnDataEntity> listOfAqicnData() {
        List<DailyAqicnDataEntity> dailyAqicnDataList = this.dailyAqicnDataRepository.findAll();
        return dailyAqicnDataList;
    }

    /**
     * Gets all the AQICN forecast data
     * @return all the AQICN forecast data
     */
    @Override
    @Cacheable(value = "forecastsdata")
    public List<ForecastEntity> listOfAqicnForecastData() {
        List<ForecastEntity> dailyAqicnForecastDataList = this.forecastRepository.findAll();
        return dailyAqicnForecastDataList;
    }


    /**
     * Save the AQICN datas into the database
     */
    @Override
    public void fillOutDailyAqicnData() throws ParseException, ExecutionException, InterruptedException, AqiNotFoundException {

        Pair<List<DailyAqicnDataEntity>, List<ForecastEntity>> dailyAndForecastAqicnDataLists = fillInDailyAndForecastAqicnData();
        List<DailyAqicnDataEntity> dailyAqicnDataEntityList = dailyAndForecastAqicnDataLists.getKey();
        List<ForecastEntity> forecastEntityList = dailyAndForecastAqicnDataLists.getValue();

        saveAqicnDataAndForecastsToDatabase(dailyAqicnDataEntityList, forecastEntityList);

    }

    @Override
    public Pair<List<DailyAqicnDataEntity>, List<ForecastEntity>> fillInDailyAndForecastAqicnData() throws ParseException, AqiNotFoundException, ExecutionException, InterruptedException {

        List<MeasureEntity> measureEntityList = this.measureRepository.findAll();
        List<StationEntity> stationEntityList = this.stationRepository.findAll();

        List<DailyAqicnDataEntity> dailyAqicnDataEntityList = Collections.synchronizedList(new ArrayList<DailyAqicnDataEntity>());
        List<ForecastEntity> forecastEntityList = Collections.synchronizedList(new ArrayList<ForecastEntity>());

        ExecutorService executor = Executors.newWorkStealingPool();
        List<Future<?>> futures = new ArrayList<Future<?>>();
        try {
            for (StationEntity station : stationEntityList) {
                futures.add(
                        executor.submit(() ->
                        {
                            try {
                                Pair<DailyAqicnDataEntity, List<ForecastEntity>> dailyAqicnAndItsForecasts = initializeDailyAqicnDataEntity(station, measureEntityList);
                                if (dailyAqicnAndItsForecasts.getKey().getStationByIdStation() != null) {
                                    dailyAqicnDataEntityList.add(dailyAqicnAndItsForecasts.getKey());
                                }
                                if (dailyAqicnAndItsForecasts.getValue().size() != 0) {
                                    forecastEntityList.addAll(dailyAqicnAndItsForecasts.getValue());
                                }


                            } catch (ParseException | AqiNotFoundException e) {
                                System.out.println("##### Error while initializing daily aqicn data entity / forecast entities : " + station.getUrl() + "#####");
                            }

                        })
                );

            }
        } catch (Exception ex) {
            // do nothing cause we still want the process to keep going. we will just ignore one of the stations that caused an error.
        }

        for (Future<?> future : futures) {
            future.get();
        }

        boolean allDone = true;
        for (Future<?> future : futures) {
            allDone &= future.isDone();
        }
        executor.shutdown();

        return new Pair<List<DailyAqicnDataEntity>, List<ForecastEntity>>(dailyAqicnDataEntityList, forecastEntityList);
    }

    private Pair<DailyAqicnDataEntity, List<ForecastEntity>> initializeDailyAqicnDataEntity(StationEntity station, List<MeasureEntity> measureEntities) throws ParseException, AqiNotFoundException {
        DailyAqicnDataEntity dailyAqicnDataEntity = new DailyAqicnDataEntity();
        List<ForecastEntity> forecastEntityList = new ArrayList<>();

        String urlStation = station.getUrl();
        System.out.println("In progress : " + urlStation);
        String dailyAqicnJsonStr = null;
        try {
            dailyAqicnJsonStr = this.aqicnHttpCaller.callExternalApi(urlStation).getBody();
        } catch (UnknownStationException | InvalidTokenException | RequestErrorException e) {
            e.printStackTrace();
        }

        if (dailyAqicnJsonStr != null) {

            try {

                JsonParser parser = new JsonParser();
                JsonObject dailyAqicnJson = parser.parse(dailyAqicnJsonStr).getAsJsonObject();

                if (DoubleUtils.tryParse(dailyAqicnJson.get("data").getAsJsonObject().get("aqi").getAsString())) {

                    GlobalObject globalObject = deserializationJsonAqicnData(dailyAqicnJsonStr);

                    dailyAqicnDataEntity.setAirQuality(globalObject.getData().getAqi());
                    if (globalObject.getData().getIaqi().getNo2() != null) {
                        dailyAqicnDataEntity.setNo2(globalObject.getData().getIaqi().getNo2().getV());
                    } else {
                        dailyAqicnDataEntity.setNo2(null);
                    }
                    if (globalObject.getData().getIaqi().getPm25() != null) {
                        dailyAqicnDataEntity.setPm25(globalObject.getData().getIaqi().getPm25().getV());
                    } else {
                        dailyAqicnDataEntity.setPm25(null);
                    }
                    if (globalObject.getData().getIaqi().getPm10() != null) {
                        dailyAqicnDataEntity.setPm10(globalObject.getData().getIaqi().getPm10().getV());
                    } else {
                        dailyAqicnDataEntity.setPm10(null);
                    }
                    if (globalObject.getData().getIaqi().getO3() != null) {
                        dailyAqicnDataEntity.setO3(globalObject.getData().getIaqi().getO3().getV());
                    } else {
                        dailyAqicnDataEntity.setO3(null);
                    }
                    if (globalObject.getData().getIaqi().getP() != null) {
                        dailyAqicnDataEntity.setPressure(globalObject.getData().getIaqi().getP().getV());
                    } else {
                        dailyAqicnDataEntity.setPressure(null);
                    }
                    if (globalObject.getData().getIaqi().getH() != null) {
                        dailyAqicnDataEntity.setHumidity(globalObject.getData().getIaqi().getH().getV());
                    } else {
                        dailyAqicnDataEntity.setHumidity(null);
                    }
                    if (globalObject.getData().getIaqi().getW() != null) {
                        dailyAqicnDataEntity.setWind(globalObject.getData().getIaqi().getW().getV());
                    } else {
                        dailyAqicnDataEntity.setWind(null);
                    }
                    if (globalObject.getData().getIaqi().getT() != null) {
                        dailyAqicnDataEntity.setTemperature(globalObject.getData().getIaqi().getT().getV());
                    } else {
                        dailyAqicnDataEntity.setTemperature(null);
                    }
                    if (globalObject.getData().getDominentpol() != null) {
                        dailyAqicnDataEntity.setDominentMeasure(globalObject.getData().getDominentpol());
                    } else {
                        dailyAqicnDataEntity.setDominentMeasure(null);
                    }
                    dailyAqicnDataEntity.setStationByIdStation(station);
                    dailyAqicnDataEntity.setIdStation(station.getIdStation());
                    dailyAqicnDataEntity.setDatetimeData(Timestamp.valueOf(globalObject.getData().getTime().getS()));

                    // for each existing measure...
                    //List<ForecastEntity> forecastEntityList = new ArrayList<>();
                    for (MeasureEntity measure : measureEntities) {

                        int numberOfForecast = 0;
                        ValuesMeasureDailyForecastData[] valuesMeasureDailyForecastData = null;

                        if (globalObject.getData().getForecast() != null) {
                            if (globalObject.getData().getForecast().getDaily() != null) {
                                switch (measure.getMeasureName()) {
                                    case "o3":
                                        numberOfForecast = globalObject.getData().getForecast().getDaily().getO3().length;
                                        valuesMeasureDailyForecastData = globalObject.getData().getForecast().getDaily().getO3();
                                        break;

                                    case "pm25":
                                        numberOfForecast = globalObject.getData().getForecast().getDaily().getPm25().length;
                                        valuesMeasureDailyForecastData = globalObject.getData().getForecast().getDaily().getPm25();
                                        break;

                                    case "pm10":
                                        numberOfForecast = globalObject.getData().getForecast().getDaily().getPm10().length;
                                        valuesMeasureDailyForecastData = globalObject.getData().getForecast().getDaily().getPm10();
                                        break;

                                    case "uvi":
                                        numberOfForecast = globalObject.getData().getForecast().getDaily().getUvi().length;
                                        valuesMeasureDailyForecastData = globalObject.getData().getForecast().getDaily().getUvi();
                                        break;
                                }

                                for (int i = 0; i < numberOfForecast; i++) {

                                    ForecastEntity forecastEntity = new ForecastEntity();
                                    forecastEntity.setIdStation(station.getIdStation());
                                    forecastEntity.setIdMeasure(measure.getIdMeasure());
                                    forecastEntity.setDateForecasted(Timestamp.valueOf(dateTimeFormat.format(new Timestamp(System.currentTimeMillis()))));
                                    forecastEntity.setIdDateForecast(new java.sql.Date(dateFormat.parse(valuesMeasureDailyForecastData[i].getDay()).getTime()));
                                    forecastEntity.setMeasureAverage(valuesMeasureDailyForecastData[i].getAvg());
                                    forecastEntity.setMeasureMax(valuesMeasureDailyForecastData[i].getMax());
                                    forecastEntity.setMeasureMin(valuesMeasureDailyForecastData[i].getMin());

                                    forecastEntity.setStationByIdStation(station);
                                    forecastEntity.setMeasureByIdMeasure(measure);

                                    forecastEntityList.add(forecastEntity);
                                }
                            }
                        }
                    }
                    return new Pair(dailyAqicnDataEntity, forecastEntityList);
                } else {
                    throw new AqiNotFoundException("AQI could not be found", DailyAqicnDataService.class);
                }
            } catch (Exception e) {

            }
        }
        return new Pair(dailyAqicnDataEntity, forecastEntityList);
    }

    private void saveAqicnDataAndForecastsToDatabase(List<DailyAqicnDataEntity> dailyAqicnDataEntityList, List<ForecastEntity> forecastEntityList) throws ExecutionException, InterruptedException {
        
        List<DailyAqicnDataEntity> dailyAqicnDataEntityListSynchronized = Collections.synchronizedList(dailyAqicnDataEntityList);
        List<ForecastEntity> forecastEntityListSynchronized = Collections.synchronizedList(forecastEntityList);

        this.dailyAqicnDataRepository.saveAll(dailyAqicnDataEntityListSynchronized);
        this.forecastRepository.saveAll(forecastEntityListSynchronized);

        this.deleteOldAqicnDatasAndForecasts();

    }

    /**
     * Delete the old aqicn data and forecasts after saving all the new datas
     */
    public void deleteOldAqicnDatasAndForecasts() {
        List<StationEntity> stationEntityList = this.stationRepository.findAll();
        for (StationEntity stationEntity : stationEntityList) {
            this.dailyAqicnDataRepository.deleteAllOldAqicnDatas(stationEntity.getIdStation());
            this.dailyAqicnDataRepository.deleteAllOldForecasts(stationEntity.getIdStation());
        }
    }


    /**
     * Gets all the AQICN data by the id of the station
     * @param idStation the id of the station
     * @return all the AQICN datas of a specific station
     */
    @Override
    public List<DailyAqicnDataEntity> getAqicnDatasByIdStation(int idStation) {
        List<DailyAqicnDataEntity> dailyAqicnDataByIdStationList = this.dailyAqicnDataRepository.findAllByIdStation(idStation);
        // we remove the stations cause if the user asks the aqicn data station with an id station,
        // we suppose he already has the station information. No need to overload the response.
        dailyAqicnDataByIdStationList.forEach(sta -> sta.setStationByIdStation(null));
        return dailyAqicnDataByIdStationList;
    }

    public List<DailyAqicnDataEntity> getaqicnDataByManyIdStations(int[] stationIds){
        List<Integer> ints = new ArrayList<>();
        for(int i : stationIds)
        {
            ints.add(i);
        }
        List<DailyAqicnDataEntity> data = this.dailyAqicnDataRepository.findAllByIdStationIn(ints);
        data.forEach(sta -> sta.setStationByIdStation(null));
        return data;
    }

    /**
     * Gets all the AQICN forecast data by the id of the station
     * @param idStation the id of the station
     * @return all the AQICN forecast datas of a specific station
     */
    @Override
    public List<ForecastEntity> getAqicnForecastDatasByIdStation(int idStation) {
        List<ForecastEntity> dailyAqicnForecastDataByIdStationList = this.forecastRepository.findAllByIdStation(idStation);
        return dailyAqicnForecastDataByIdStationList;
    }

    /**
     * Gets all the AQICN forecast data by the id station and the measure name
     * @param idStation the id of the station
     * @param measureName the name of the measure
     * @return all the AQICN forecast datas of a specific station and a specific measure
     */
    @Override
    public List<ForecastEntity> getAqicnForecastDatasByIdStationAndMeasureName(int idStation, String measureName) {
        List<ForecastEntity> dailyAqicnForecastDataByIdStationAndMeasureList = this.forecastRepository.findAllByIdStationAndMeasureByIdMeasure_MeasureName(idStation, measureName);
        return dailyAqicnForecastDataByIdStationAndMeasureList;
    }

    @Override
    public GlobalObject deserializationJsonAqicnData(String jsonAqicnData) {
        Gson gson = new Gson();
        return gson.fromJson(jsonAqicnData, GlobalObject.class);

    }

}


