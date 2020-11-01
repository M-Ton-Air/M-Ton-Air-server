package com.polytech.mtonairserver.service.implementation;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.polytech.mtonairserver.controller.AqicnController;
import com.polytech.mtonairserver.customexceptions.requestaqicnexception.InvalidTokenException;
import com.polytech.mtonairserver.customexceptions.requestaqicnexception.UnknownStationException;
import com.polytech.mtonairserver.model.entities.*;
import com.polytech.mtonairserver.model.responses.ApiSuccessResponse;
import com.polytech.mtonairserver.repository.DailyAqicnDataRepository;
import com.polytech.mtonairserver.repository.ForecastRepository;
import com.polytech.mtonairserver.repository.MeasureRepository;
import com.polytech.mtonairserver.repository.StationRepository;
import com.polytech.mtonairserver.service.interfaces.IDailyAqicnDataService;
import com.polytech.mtonairserver.service.interfaces.IStationService;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Service implementation for the daily AQICN data.
 */
@Service
public class DailyAqicnDataService implements IDailyAqicnDataService {

    private DailyAqicnDataRepository dailyAqicnDataRepository;

    @Autowired
    private AqicnService aqicnService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private MeasureRepository measureRepository;

    @Autowired
    private ForecastRepository forecastRepository;

    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    public DailyAqicnDataService(DailyAqicnDataRepository dailyAqicnDataRepository) {
        this.dailyAqicnDataRepository = dailyAqicnDataRepository;
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
     * @throws UnknownStationException
     * @throws InvalidTokenException
     * @throws ParseException
     */
    @Override
    public void fillOutDailyAqicnData() throws UnknownStationException, InvalidTokenException, ParseException {

        List<StationEntity> stationEntityList = this.stationRepository.findAll();
        List<MeasureEntity> measureEntityList = this.measureRepository.findAll();
        AqicnController aqicnController = new AqicnController(aqicnService);

        // for each existing station : AQICN datas are recorded in the database 'daily_aqicn_data' and the forecast in the database 'forecast'
        for (StationEntity station : stationEntityList) {
            String urlStation = station.getUrl();
            ResponseEntity<String> stringResponseEntity = aqicnService.requestAqicn(urlStation, this.request);
            String bodyStringResponseEntity = stringResponseEntity.getBody();
            DailyAqicnDataEntity dailyAqicnDataEntity = new DailyAqicnDataEntity();
            JsonObject convertedObject = new Gson().fromJson(bodyStringResponseEntity, JsonObject.class);

            if (convertedObject.get("data").getAsJsonObject().get("aqi") != null) {
                dailyAqicnDataEntity.setAirQuality(convertedObject.get("data").getAsJsonObject().get("aqi").getAsDouble());
            }
            if (convertedObject.get("data").getAsJsonObject().get("iaqi").getAsJsonObject().get("no2") != null) {
                dailyAqicnDataEntity.setNo2(convertedObject.get("data").getAsJsonObject().get("iaqi").getAsJsonObject().get("no2").getAsJsonObject().get("v").getAsDouble());
            }
            if (convertedObject.get("data").getAsJsonObject().get("iaqi").getAsJsonObject().get("pm25") != null) {
                dailyAqicnDataEntity.setPm25(convertedObject.get("data").getAsJsonObject().get("iaqi").getAsJsonObject().get("pm25").getAsJsonObject().get("v").getAsDouble());
            }
            if (convertedObject.get("data").getAsJsonObject().get("iaqi").getAsJsonObject().get("pm10") != null) {
                dailyAqicnDataEntity.setPm10(convertedObject.get("data").getAsJsonObject().get("iaqi").getAsJsonObject().get("pm10").getAsJsonObject().get("v").getAsDouble());
            }
            if (convertedObject.get("data").getAsJsonObject().get("iaqi").getAsJsonObject().get("o3") != null) {
                dailyAqicnDataEntity.setO3(convertedObject.get("data").getAsJsonObject().get("iaqi").getAsJsonObject().get("o3").getAsJsonObject().get("v").getAsDouble());
            }
            if (convertedObject.get("data").getAsJsonObject().get("iaqi").getAsJsonObject().get("p") != null) {
                dailyAqicnDataEntity.setPressure(convertedObject.get("data").getAsJsonObject().get("iaqi").getAsJsonObject().get("p").getAsJsonObject().get("v").getAsDouble());
            }
            if (convertedObject.get("data").getAsJsonObject().get("iaqi").getAsJsonObject().get("h") != null) {
                dailyAqicnDataEntity.setHumidity(convertedObject.get("data").getAsJsonObject().get("iaqi").getAsJsonObject().get("h").getAsJsonObject().get("v").getAsDouble());
            }
            if (convertedObject.get("data").getAsJsonObject().get("iaqi").getAsJsonObject().get("w") != null) {
                dailyAqicnDataEntity.setWind(convertedObject.get("data").getAsJsonObject().get("iaqi").getAsJsonObject().get("w").getAsJsonObject().get("v").getAsDouble());
            }
            if (convertedObject.get("data").getAsJsonObject().get("iaqi").getAsJsonObject().get("t") != null) {
                dailyAqicnDataEntity.setTemperature(convertedObject.get("data").getAsJsonObject().get("iaqi").getAsJsonObject().get("t").getAsJsonObject().get("v").getAsDouble());
            }
            dailyAqicnDataEntity.setStationByIdStation(station);
            dailyAqicnDataEntity.setIdStation(station.getIdStation());
            dailyAqicnDataEntity.setDatetimeData(Timestamp.valueOf(convertedObject.get("data").getAsJsonObject().get("time").getAsJsonObject().get("s").getAsString()));

            this.dailyAqicnDataRepository.save(dailyAqicnDataEntity);

            // for each existing measure...
            for (MeasureEntity measure : measureEntityList) {

                int numberOfForecast = convertedObject.get("data").getAsJsonObject().get("forecast").getAsJsonObject().get("daily").getAsJsonObject().get(measure.getMeasureName()).getAsJsonArray().size();

                // for each forecast...
                for (int i = 0; i < numberOfForecast; i++) {
                    String count = String.valueOf(i);
                    ForecastEntity forecastEntity = new ForecastEntity();

                    forecastEntity.setIdStation(station.getIdStation());
                    forecastEntity.setIdMeasure(measure.getIdMeasure());
                    forecastEntity.setDateForecasted(Timestamp.valueOf(dateTimeFormat.format(new Timestamp(System.currentTimeMillis()))));
                    forecastEntity.setIdDateForecast(new java.sql.Date(dateFormat.parse(convertedObject.get("data").getAsJsonObject().get("forecast").getAsJsonObject().get("daily").getAsJsonObject().get(measure.getMeasureName()).getAsJsonArray().get(Integer.parseInt(count)).getAsJsonObject().get("day").getAsString()).getTime()));
                    forecastEntity.setMeasureAverage(convertedObject.get("data").getAsJsonObject().get("forecast").getAsJsonObject().get("daily").getAsJsonObject().get(measure.getMeasureName()).getAsJsonArray().get(Integer.parseInt(count)).getAsJsonObject().get("avg").getAsDouble());
                    forecastEntity.setMeasureMax(convertedObject.get("data").getAsJsonObject().get("forecast").getAsJsonObject().get("daily").getAsJsonObject().get(measure.getMeasureName()).getAsJsonArray().get(Integer.parseInt(count)).getAsJsonObject().get("max").getAsDouble());
                    forecastEntity.setMeasureMin(convertedObject.get("data").getAsJsonObject().get("forecast").getAsJsonObject().get("daily").getAsJsonObject().get(measure.getMeasureName()).getAsJsonArray().get(Integer.parseInt(count)).getAsJsonObject().get("min").getAsDouble());
                    forecastEntity.setStationByIdStation(station);
                    forecastEntity.setMeasureByIdMeasure(measure);

                    this.forecastRepository.save(forecastEntity);
                }
            }
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


