package com.polytech.mtonairserver.service;

import com.polytech.mtonairserver.customexceptions.requestaqicnexception.CoordinatesRetrievalException;
import com.polytech.mtonairserver.customexceptions.requestaqicnexception.InvalidTokenException;
import com.polytech.mtonairserver.customexceptions.requestaqicnexception.RequestErrorException;
import com.polytech.mtonairserver.customexceptions.requestaqicnexception.UnknownStationException;
import com.polytech.mtonairserver.model.ReponseObject.CityData;
import com.polytech.mtonairserver.service.implementation.GeoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.concurrent.ExecutionException;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class GeoServiceTest
{
    @Autowired
    GeoService geoService;

    @Test
    public void getAllAqicnStationsCoordinatesTest() throws InterruptedException, ExecutionException, InvalidTokenException, CoordinatesRetrievalException, RequestErrorException, UnknownStationException
    {
        // do not start unless you have 10min to lose
        // + it does query aqicn for nothing.
        //List<CityData> citiesData = this.geoService.getAllAqicnStationsCoordinates();
    }

}
