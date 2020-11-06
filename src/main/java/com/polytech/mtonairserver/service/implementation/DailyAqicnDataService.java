package com.polytech.mtonairserver.service.implementation;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.polytech.mtonairserver.customexceptions.requestaqicnexception.InvalidTokenException;
import com.polytech.mtonairserver.customexceptions.requestaqicnexception.RequestErrorException;
import com.polytech.mtonairserver.customexceptions.requestaqicnexception.UnknownStationException;
import com.polytech.mtonairserver.external.aqicn.AqicnHttpCaller;
import com.polytech.mtonairserver.model.entities.*;
import com.polytech.mtonairserver.repository.DailyAqicnDataRepository;
import com.polytech.mtonairserver.repository.ForecastRepository;
import com.polytech.mtonairserver.repository.MeasureRepository;
import com.polytech.mtonairserver.repository.StationRepository;
import com.polytech.mtonairserver.service.interfaces.IDailyAqicnDataService;
import com.polytech.mtonairserver.utils.doubleextensions.DoubleUtils;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Service implementation for the daily AQICN data.
 */
@Service
public class DailyAqicnDataService implements IDailyAqicnDataService {

    private DailyAqicnDataRepository dailyAqicnDataRepository;

    private StationRepository stationRepository;

    private MeasureRepository measureRepository;

    private ForecastRepository forecastRepository;

    private AqicnHttpCaller aqicnHttpCaller;

    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

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
    public List<DailyAqicnDataEntity> listOfAqicnData() {
        List<DailyAqicnDataEntity> dailyAqicnDataList = this.dailyAqicnDataRepository.findAll();
        dailyAqicnDataList.forEach(sta -> sta.getStationByIdStation().getDailyAqicnDataByIdStation().clear());
        dailyAqicnDataList.forEach(st -> st.getStationByIdStation().getForecastsByIdStation().clear());
        return dailyAqicnDataList;
    }

    /**
     * Gets all the AQICN forecast data
     * @return all the AQICN forecast data
     */
    @Override
    public List<ForecastEntity> listOfAqicnForecastData() {
        List<ForecastEntity> dailyAqicnForecastDataList = this.forecastRepository.findAll();
        dailyAqicnForecastDataList.forEach(sta -> sta.getStationByIdStation().getDailyAqicnDataByIdStation().clear());
        dailyAqicnForecastDataList.forEach(st -> st.getStationByIdStation().getForecastsByIdStation().clear());
        return dailyAqicnForecastDataList;
    }


    /**
     * Save the AQICN datas into the database
     */
    @Override
    public void fillOutDailyAqicnData() throws ParseException, ExecutionException, InterruptedException {

        Pair<List<DailyAqicnDataEntity>, List<ForecastEntity>> dailyAndForecastAqicnDataLists = fillInDailyAndForecastAqicnData();
        List<DailyAqicnDataEntity> dailyAqicnDataEntityList = dailyAndForecastAqicnDataLists.getKey();
        List<ForecastEntity> forecastEntityList = dailyAndForecastAqicnDataLists.getValue();

        saveAqicnDataAndForecastsToDatabase(dailyAqicnDataEntityList, forecastEntityList);

    }



    /**
     * Fill in a list of DailyAqicnDataEntity and a list of forecastEntity
     * @return a Pair containing the dailyAqicnDataEntityList and the forecastEntityList
     * @throws ParseException
     */
    private Pair<List<DailyAqicnDataEntity>, List<ForecastEntity>> fillInDailyAndForecastAqicnData() throws ParseException, ExecutionException, InterruptedException {

        //int THREADS_COUNT=100;

        List<MeasureEntity> measureEntityList = this.measureRepository.findAll();
        List<StationEntity> stationEntityList = this.stationRepository.findAll();

        List<DailyAqicnDataEntity> dailyAqicnDataEntityList = new ArrayList<DailyAqicnDataEntity>();
        List<ForecastEntity> forecastEntityList = new ArrayList<ForecastEntity>();

        // for each existing station : AQICN datas are recorded in the database 'daily_aqicn_data' and the forecast in the database 'forecast'

        ExecutorService executor = Executors.newWorkStealingPool();
        List<Future<?>> futures = new ArrayList<Future<?>>();





        for (StationEntity station : stationEntityList) {

          //  ExecutorService executorService = Executors.newFixedThreadPool(THREADS_COUNT);
            futures.add(
                    executor.submit( () ->
                    {
                        String urlStation = station.getUrl();


                        JsonObject dailyAqicnJson = null;
                        try {
                            dailyAqicnJson = this.aqicnHttpCaller.callExternalApi(urlStation);
                        } catch (UnknownStationException | InvalidTokenException | RequestErrorException e) {
                            e.printStackTrace();
                        }
                        if (dailyAqicnJson != null) {
                            DailyAqicnDataEntity dailyAqicnDataEntity = new DailyAqicnDataEntity();
                            if (DoubleUtils.tryParse(dailyAqicnJson.get("data").getAsJsonObject().get("aqi").getAsString())) {
                                if (dailyAqicnJson.get("data").getAsJsonObject().get("aqi") != null) {
                                    //if (Double.(convertedObject.get("data").getAsJsonObject().get("aqi").getAsDouble() != double)
                                    dailyAqicnDataEntity.setAirQuality(dailyAqicnJson.get("data").getAsJsonObject().get("aqi").getAsDouble());
                                } else {
                                    dailyAqicnDataEntity.setAirQuality(null);
                                }
                                if (dailyAqicnJson.get("data").getAsJsonObject().get("iaqi").getAsJsonObject().get("no2") != null) {
                                    dailyAqicnDataEntity.setNo2(dailyAqicnJson.get("data").getAsJsonObject().get("iaqi").getAsJsonObject().get("no2").getAsJsonObject().get("v").getAsDouble());
                                } else {
                                    dailyAqicnDataEntity.setNo2(null);
                                }
                                if (dailyAqicnJson.get("data").getAsJsonObject().get("iaqi").getAsJsonObject().get("pm25") != null) {
                                    dailyAqicnDataEntity.setPm25(dailyAqicnJson.get("data").getAsJsonObject().get("iaqi").getAsJsonObject().get("pm25").getAsJsonObject().get("v").getAsDouble());
                                } else {
                                    dailyAqicnDataEntity.setPm25(null);
                                }
                                if (dailyAqicnJson.get("data").getAsJsonObject().get("iaqi").getAsJsonObject().get("pm10") != null) {
                                    dailyAqicnDataEntity.setPm10(dailyAqicnJson.get("data").getAsJsonObject().get("iaqi").getAsJsonObject().get("pm10").getAsJsonObject().get("v").getAsDouble());
                                } else {
                                    dailyAqicnDataEntity.setPm10(null);
                                }
                                if (dailyAqicnJson.get("data").getAsJsonObject().get("iaqi").getAsJsonObject().get("o3") != null) {
                                    dailyAqicnDataEntity.setO3(dailyAqicnJson.get("data").getAsJsonObject().get("iaqi").getAsJsonObject().get("o3").getAsJsonObject().get("v").getAsDouble());
                                } else {
                                    dailyAqicnDataEntity.setO3(null);
                                }
                                if (dailyAqicnJson.get("data").getAsJsonObject().get("iaqi").getAsJsonObject().get("p") != null) {
                                    dailyAqicnDataEntity.setPressure(dailyAqicnJson.get("data").getAsJsonObject().get("iaqi").getAsJsonObject().get("p").getAsJsonObject().get("v").getAsDouble());
                                } else {
                                    dailyAqicnDataEntity.setPressure(null);
                                }
                                if (dailyAqicnJson.get("data").getAsJsonObject().get("iaqi").getAsJsonObject().get("h") != null) {
                                    dailyAqicnDataEntity.setHumidity(dailyAqicnJson.get("data").getAsJsonObject().get("iaqi").getAsJsonObject().get("h").getAsJsonObject().get("v").getAsDouble());
                                } else {
                                    dailyAqicnDataEntity.setHumidity(null);
                                }
                                if (dailyAqicnJson.get("data").getAsJsonObject().get("iaqi").getAsJsonObject().get("w") != null) {
                                    dailyAqicnDataEntity.setWind(dailyAqicnJson.get("data").getAsJsonObject().get("iaqi").getAsJsonObject().get("w").getAsJsonObject().get("v").getAsDouble());
                                } else {
                                    dailyAqicnDataEntity.setWind(null);
                                }
                                if (dailyAqicnJson.get("data").getAsJsonObject().get("iaqi").getAsJsonObject().get("t") != null) {
                                    dailyAqicnDataEntity.setTemperature(dailyAqicnJson.get("data").getAsJsonObject().get("iaqi").getAsJsonObject().get("t").getAsJsonObject().get("v").getAsDouble());
                                } else {
                                    dailyAqicnDataEntity.setTemperature(null);
                                }
                                dailyAqicnDataEntity.setStationByIdStation(station);
                                dailyAqicnDataEntity.setIdStation(station.getIdStation());
                                dailyAqicnDataEntity.setDatetimeData(Timestamp.valueOf(dailyAqicnJson.get("data").getAsJsonObject().get("time").getAsJsonObject().get("s").getAsString()));


                                dailyAqicnDataEntityList.add(dailyAqicnDataEntity);

                                // for each existing measure...
                                for (MeasureEntity measure : measureEntityList) {

                                    int numberOfForecast = dailyAqicnJson.get("data").getAsJsonObject().get("forecast").getAsJsonObject().get("daily").getAsJsonObject().get(measure.getMeasureName()).getAsJsonArray().size();

                                    // for each forecast...
                                    for (int i = 0; i < numberOfForecast; i++) {
                                        String count = String.valueOf(i);
                                        ForecastEntity forecastEntity = new ForecastEntity();

                                        forecastEntity.setIdStation(station.getIdStation());
                                        forecastEntity.setIdMeasure(measure.getIdMeasure());
                                        forecastEntity.setDateForecasted(Timestamp.valueOf(dateTimeFormat.format(new Timestamp(System.currentTimeMillis()))));
                                        try {
                                            forecastEntity.setIdDateForecast(new java.sql.Date(dateFormat.parse(dailyAqicnJson.get("data").getAsJsonObject().get("forecast").getAsJsonObject().get("daily").getAsJsonObject().get(measure.getMeasureName()).getAsJsonArray().get(Integer.parseInt(count)).getAsJsonObject().get("day").getAsString()).getTime()));
                                        } catch (ParseException e) {
                                            //e.printStackTrace();
                                        }
                                        forecastEntity.setMeasureAverage(dailyAqicnJson.get("data").getAsJsonObject().get("forecast").getAsJsonObject().get("daily").getAsJsonObject().get(measure.getMeasureName()).getAsJsonArray().get(Integer.parseInt(count)).getAsJsonObject().get("avg").getAsDouble());
                                        forecastEntity.setMeasureMax(dailyAqicnJson.get("data").getAsJsonObject().get("forecast").getAsJsonObject().get("daily").getAsJsonObject().get(measure.getMeasureName()).getAsJsonArray().get(Integer.parseInt(count)).getAsJsonObject().get("max").getAsDouble());
                                        forecastEntity.setMeasureMin(dailyAqicnJson.get("data").getAsJsonObject().get("forecast").getAsJsonObject().get("daily").getAsJsonObject().get(measure.getMeasureName()).getAsJsonArray().get(Integer.parseInt(count)).getAsJsonObject().get("min").getAsDouble());
                                        forecastEntity.setStationByIdStation(station);
                                        forecastEntity.setMeasureByIdMeasure(measure);

                                        forecastEntityList.add(forecastEntity);
                                    }
                                }

                            }
                        }
                    }));

        }

        for (Future<?> future : futures) {
            future.get();
        }

        boolean allDone = true;
        for(Future<?> future : futures)
        {
            allDone &= future.isDone();
        }
        executor.shutdown();


        return new Pair<List<DailyAqicnDataEntity>, List<ForecastEntity>>(dailyAqicnDataEntityList, forecastEntityList);
    }


    private void saveAqicnDataAndForecastsToDatabase(List<DailyAqicnDataEntity> dailyAqicnDataEntityList, List<ForecastEntity> forecastEntityList) {
        for (DailyAqicnDataEntity dailyAqicnDataEntity : dailyAqicnDataEntityList) {
            this.dailyAqicnDataRepository.save(dailyAqicnDataEntity);
        }

        for (ForecastEntity forecastEntity : forecastEntityList) {
            this.forecastRepository.save(forecastEntity);
        }
    }


    /**
     * Gets all the AQICN data by the id of the station
     * @param idStation the id of the station
     * @return all the AQICN datas of a specific station
     */
    @Override
    public List<DailyAqicnDataEntity> getAqicnDatasByIdStation(int idStation) {
        List<DailyAqicnDataEntity> dailyAqicnDataByIdStationList = this.dailyAqicnDataRepository.findByIdStation(idStation);
        dailyAqicnDataByIdStationList.forEach(sta -> sta.getStationByIdStation().getDailyAqicnDataByIdStation().clear());
        dailyAqicnDataByIdStationList.forEach(st -> st.getStationByIdStation().getForecastsByIdStation().clear());
        return dailyAqicnDataByIdStationList;
    }

    /**
     * Gets all the AQICN forecast data by the id of the station
     * @param idStation the id of the station
     * @return all the AQICN forecast datas of a specific station
     */
    @Override
    public List<ForecastEntity> getAqicnForecastDatasByIdStation(int idStation) {
        List<ForecastEntity> dailyAqicnForecastDataByIdStationList = this.forecastRepository.findByIdStation(idStation);
        dailyAqicnForecastDataByIdStationList.forEach(sta -> sta.getStationByIdStation().getDailyAqicnDataByIdStation().clear());
        dailyAqicnForecastDataByIdStationList.forEach(st -> st.getStationByIdStation().getForecastsByIdStation().clear());
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
        List<ForecastEntity> dailyAqicnForecastDataByIdStationAndMeasureList = this.forecastRepository.findByIdStationAndMeasureByIdMeasure_MeasureName(idStation, measureName);
        dailyAqicnForecastDataByIdStationAndMeasureList.forEach(sta -> sta.getStationByIdStation().getDailyAqicnDataByIdStation().clear());
        dailyAqicnForecastDataByIdStationAndMeasureList.forEach(st -> st.getStationByIdStation().getForecastsByIdStation().clear());
        return dailyAqicnForecastDataByIdStationAndMeasureList;
    }

    }


