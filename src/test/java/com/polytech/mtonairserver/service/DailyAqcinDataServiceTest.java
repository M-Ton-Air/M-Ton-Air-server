package com.polytech.mtonairserver.service;

import com.polytech.mtonairserver.external.aqicn.AqicnHttpCaller;
import com.polytech.mtonairserver.repository.DailyAqicnDataRepository;
import com.polytech.mtonairserver.repository.ForecastRepository;
import com.polytech.mtonairserver.repository.MeasureRepository;
import com.polytech.mtonairserver.repository.StationRepository;
import com.polytech.mtonairserver.service.implementation.DailyAqicnDataService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.util.concurrent.ExecutionException;

@ExtendWith(MockitoExtension.class)
public class DailyAqcinDataServiceTest
{
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
    public void fillOutDailyAqicnDataTest() throws ParseException, ExecutionException, InterruptedException {
        //todo
        this.dailyAqicnDataService.fillOutDailyAqicnData();
    }
}
