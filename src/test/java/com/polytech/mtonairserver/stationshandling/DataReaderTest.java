package com.polytech.mtonairserver.stationshandling;

import com.polytech.mtonairserver.customexceptions.datareader.NoProperLocationFoundException;
import com.polytech.mtonairserver.customexceptions.datareader.UnsupportedFindOperationOnLocationException;
import com.polytech.mtonairserver.model.entities.StationEntity;
import com.polytech.mtonairserver.stationshandling.io.DataReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * In order to work, this test must be launched along with DataReaderPArticu
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureTestDatabase
public class DataReaderTest
{
    @Autowired
    private DataReader dataReader;

    private static boolean setupIsDone = false;

    protected static List<StationEntity> stationEntities;


    @Before
    public void setUp() throws NoProperLocationFoundException, UnsupportedFindOperationOnLocationException, IOException, ExecutionException, InterruptedException
    {
        if(setupIsDone)
        {
            return;
        }

        stationEntities = dataReader.initializeAllStationsFromAqicnStationsHtmlFile();
        setupIsDone = true;
    }

    @Test
    public void retrieveAllStationNames()
    {
        Map<String, String> countriesWithTheirIso2 = this.getAllCountriesAndTheirIso2();

        for(StationEntity station : stationEntities)
        {
            String expectedCountry = countriesWithTheirIso2.get(station.getIso2());
            Assert.assertEquals(expectedCountry.toLowerCase(), station.getCountry().toLowerCase());
        }
    }

    private Map<String, String> getAllCountriesAndTheirIso2()
    {
        Map<String, String> countries = new HashMap<String, String>();
        for (String iso2 : Locale.getISOCountries())
        {
            Locale loc = new Locale("", iso2);
            countries.put(iso2, new Locale("en", iso2).getDisplayCountry(new Locale("en", iso2)));
        }
        countries.put("XK", "Kosovo");
        return countries;
    }
}