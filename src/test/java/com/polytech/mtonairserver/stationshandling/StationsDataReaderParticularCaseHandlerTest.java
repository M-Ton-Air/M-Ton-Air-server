package com.polytech.mtonairserver.stationshandling;

import com.polytech.mtonairserver.customexceptions.datareader.NoProperLocationFoundException;
import com.polytech.mtonairserver.customexceptions.datareader.UnsupportedFindOperationOnLocationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureTestDatabase
public class StationsDataReaderParticularCaseHandlerTest extends StationsDataReaderTest
{
    @Test
    public void executeAllCleaningMethods() throws IOException, NoProperLocationFoundException, UnsupportedFindOperationOnLocationException
    {
        Assert.assertTrue(StationsDataReaderTest.stationEntities.stream().anyMatch(c -> c.getCountry().toLowerCase().equals(("Georgia").toLowerCase())));
        Assert.assertTrue(StationsDataReaderTest.stationEntities.stream().anyMatch(c -> c.getCountry().toLowerCase().equals(("Afghanistan").toLowerCase())));
        Assert.assertTrue(StationsDataReaderTest.stationEntities.stream().anyMatch(c -> c.getCountry().toLowerCase().equals(("El Salvador").toLowerCase())));
        Assert.assertTrue(StationsDataReaderTest.stationEntities.stream().anyMatch(c -> c.getCountry().toLowerCase().equals(("United Arab Emirates").toLowerCase())));
        Assert.assertTrue(StationsDataReaderTest.stationEntities.stream().anyMatch(c -> c.getCountry().toLowerCase().equals(("Hong Kong").toLowerCase())));
        Assert.assertTrue(StationsDataReaderTest.stationEntities.stream().anyMatch(c -> c.getCountry().toLowerCase().equals(("Côte D'Ivoire").toLowerCase())));
        Assert.assertTrue(StationsDataReaderTest.stationEntities.stream().anyMatch(c -> c.getCountry().toLowerCase().equals(("South Korea").toLowerCase())));
        Assert.assertTrue(StationsDataReaderTest.stationEntities.stream().anyMatch(c -> c.getCountry().toLowerCase().equals(("Macao").toLowerCase())));
        Assert.assertTrue(StationsDataReaderTest.stationEntities.stream().anyMatch(c -> c.getCountry().toLowerCase().equals(("Myanmar").toLowerCase())));
        Assert.assertTrue(StationsDataReaderTest.stationEntities.stream().anyMatch(c -> c.getCountry().toLowerCase().equals(("Netherlands").toLowerCase())));
        Assert.assertTrue(StationsDataReaderTest.stationEntities.stream().anyMatch(c -> c.getCountry().toLowerCase().equals(("Czech Republic").toLowerCase())));
        Assert.assertTrue(StationsDataReaderTest.stationEntities.stream().anyMatch(c -> c.getCountry().toLowerCase().equals(("Bosnia And Herzegovina").toLowerCase())));


        Assert.assertTrue(StationsDataReaderTest.stationEntities.stream().anyMatch(c -> c.getSubdivision1() != null && c.getSubdivision1().toLowerCase().equals(("Henan").toLowerCase())));
        Assert.assertTrue(StationsDataReaderTest.stationEntities.stream().anyMatch(c -> c.getSubdivision1() != null && c.getSubdivision1().toLowerCase().equals(("Massachusetts").toLowerCase())));

        Assert.assertTrue(StationsDataReaderTest.stationEntities.stream().anyMatch(c -> c.getStationName() != null && c.getStationName().toLowerCase().equals(("Tel Aviv Yafo").toLowerCase())));
        Assert.assertTrue(StationsDataReaderTest.stationEntities.stream().anyMatch(c -> c.getStationName() != null && c.getStationName().toLowerCase().equals(("Sankt Gallen").toLowerCase())));
        Assert.assertTrue(StationsDataReaderTest.stationEntities.stream().anyMatch(c -> c.getStationName() != null && c.getStationName().toLowerCase().equals(("Los Angeles").toLowerCase())));


        Assert.assertFalse(StationsDataReaderTest.stationEntities.stream().anyMatch(c -> c.getSubdivision1() != null && c.getSubdivision1().contains("-")));
        Assert.assertFalse(StationsDataReaderTest.stationEntities.stream().anyMatch(c -> c.getStationName() != null && c.getStationName().contains("-")));
    }
}