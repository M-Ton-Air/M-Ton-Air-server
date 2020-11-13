package com.polytech.mtonairserver.service;

import com.polytech.mtonairserver.customexceptions.requestaqicnexception.AqiNotFoundException;
import com.polytech.mtonairserver.external.aqicn.AqicnHttpCaller;
import com.polytech.mtonairserver.model.responseobject.GlobalObject;
import com.polytech.mtonairserver.model.entities.DailyAqicnDataEntity;
import com.polytech.mtonairserver.model.entities.ForecastEntity;
import com.polytech.mtonairserver.repository.DailyAqicnDataRepository;
import com.polytech.mtonairserver.repository.ForecastRepository;
import com.polytech.mtonairserver.repository.MeasureRepository;
import com.polytech.mtonairserver.repository.StationRepository;
import com.polytech.mtonairserver.service.implementation.DailyAqicnDataService;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase
public class DailyAqcinDataServiceTest
{
    public static final String NORMAL_JSON_RESPONSE = "{\"status\":\"ok\",\"data\":{\"aqi\":45,\"idx\":10050,\"attributions\":[{\"url\":\"http:\\/\\/www.air-rhonealpes.fr\\/\",\"name\":\"Observatoire Air Rhone-Alpes\",\"logo\":\"France-AirRhoneAlpes.png\"},{\"url\":\"https:\\/\\/waqi.info\\/\",\"name\":\"World Air Quality Index Project\"}],\"city\":{\"geo\":[45.74972628,4.84439392],\"name\":\"Lyon Trafic Jaur\\u00e8s, France\",\"url\":\"https:\\/\\/aqicn.org\\/city\\/france\\/rhonealpes\\/rhone\\/lyon-trafic-jaures\"},\"dominentpol\":\"pm10\",\"iaqi\":{\"dew\":{\"v\":8},\"h\":{\"v\":67},\"no2\":{\"v\":15.3},\"p\":{\"v\":1028},\"pm10\":{\"v\":45},\"t\":{\"v\":14},\"w\":{\"v\":1.5}},\"time\":{\"s\":\"2020-11-06 11:00:00\",\"tz\":\"+01:00\",\"v\":1604660400,\"iso\":\"2020-11-06T11:00:00+01:00\"},\"forecast\":{\"daily\":{\"o3\":[{\"avg\":21,\"day\":\"2020-11-04\",\"max\":24,\"min\":18},{\"avg\":8,\"day\":\"2020-11-05\",\"max\":19,\"min\":2},{\"avg\":11,\"day\":\"2020-11-06\",\"max\":29,\"min\":1},{\"avg\":22,\"day\":\"2020-11-07\",\"max\":29,\"min\":17},{\"avg\":17,\"day\":\"2020-11-08\",\"max\":28,\"min\":8},{\"avg\":13,\"day\":\"2020-11-09\",\"max\":27,\"min\":5},{\"avg\":7,\"day\":\"2020-11-10\",\"max\":8,\"min\":7}],\"pm10\":[{\"avg\":9,\"day\":\"2020-11-04\",\"max\":12,\"min\":7},{\"avg\":19,\"day\":\"2020-11-05\",\"max\":23,\"min\":12},{\"avg\":24,\"day\":\"2020-11-06\",\"max\":37,\"min\":11},{\"avg\":13,\"day\":\"2020-11-07\",\"max\":17,\"min\":11},{\"avg\":13,\"day\":\"2020-11-08\",\"max\":16,\"min\":9},{\"avg\":21,\"day\":\"2020-11-09\",\"max\":24,\"min\":16},{\"avg\":25,\"day\":\"2020-11-10\",\"max\":25,\"min\":25}],\"pm25\":[{\"avg\":28,\"day\":\"2020-11-04\",\"max\":36,\"min\":20},{\"avg\":58,\"day\":\"2020-11-05\",\"max\":67,\"min\":35},{\"avg\":70,\"day\":\"2020-11-06\",\"max\":93,\"min\":44},{\"avg\":40,\"day\":\"2020-11-07\",\"max\":47,\"min\":31},{\"avg\":39,\"day\":\"2020-11-08\",\"max\":48,\"min\":29},{\"avg\":54,\"day\":\"2020-11-09\",\"max\":62,\"min\":39},{\"avg\":63,\"day\":\"2020-11-10\",\"max\":63,\"min\":63}],\"uvi\":[{\"avg\":0,\"day\":\"2020-11-04\",\"max\":1,\"min\":0},{\"avg\":0,\"day\":\"2020-11-05\",\"max\":1,\"min\":0},{\"avg\":0,\"day\":\"2020-11-06\",\"max\":2,\"min\":0},{\"avg\":0,\"day\":\"2020-11-07\",\"max\":1,\"min\":0},{\"avg\":0,\"day\":\"2020-11-08\",\"max\":1,\"min\":0},{\"avg\":0,\"day\":\"2020-11-09\",\"max\":1,\"min\":0},{\"avg\":0,\"day\":\"2020-11-10\",\"max\":1,\"min\":0},{\"avg\":0,\"day\":\"2020-11-11\",\"max\":0,\"min\":0}]}},\"debug\":{\"sync\":\"2020-11-06T20:22:32+09:00\"}}}";

    @Mock
    private DailyAqicnDataRepository dailyAqicnDataRepository;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private MeasureRepository measureRepository;

    @Mock
    private ForecastRepository forecastRepository;

    @Mock
    private AqicnHttpCaller aqicnHttpCaller;

    @InjectMocks
    private DailyAqicnDataService dailyAqicnDataService;

    @Test
    public void fillOutDailyAqicnDataTest() throws ParseException, ExecutionException, InterruptedException, AqiNotFoundException {
        //todo
        this.dailyAqicnDataService.fillOutDailyAqicnData();
    }

    @Test
    public void deserializationJsonAqicnDataTest() {
        GlobalObject globalObject = this.dailyAqicnDataService.deserializationJsonAqicnData(NORMAL_JSON_RESPONSE);

        assertEquals(45, globalObject.getData().getAqi());
        System.out.println("= Test 1 passed : The air quality indicator is the right one ! ✅ =");

        assertEquals("Lyon Trafic Jaurès, France", globalObject.getData().getCity().getName());
        System.out.println("= Test 2 passed : The station name is the right one ! ✅ =");

        assertEquals("pm10", globalObject.getData().getDominentpol());
        System.out.println("= Test 3 passed : The dominant measure is the right one ! ✅ =");
    }

    @Test
    public void fillInDailyAndForecastAqicnDataTestTest() throws ParseException, InterruptedException, ExecutionException, AqiNotFoundException {

        Pair<List<DailyAqicnDataEntity>, List<ForecastEntity>> pair = this.dailyAqicnDataService.fillInDailyAndForecastAqicnData();

    }
}
