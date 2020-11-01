package com.polytech.mtonairserver.stationshandling;

import com.polytech.mtonairserver.customexceptions.datareader.NoProperLocationFoundException;
import com.polytech.mtonairserver.customexceptions.datareader.UnsupportedFindOperationOnLocationException;
import com.polytech.mtonairserver.model.entities.StationEntity;
import com.polytech.mtonairserver.stationshandling.io.DataReader;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.List;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureTestDatabase
public class DataReaderParticularCaseHandlerTest extends DataReaderTest
{
    @Test
    public void executeAllCleaningMethods() throws IOException, NoProperLocationFoundException, UnsupportedFindOperationOnLocationException
    {
        Assert.assertTrue(this.stationEntities.stream().anyMatch(c -> c.getCountry().toLowerCase().equals(("Georgia").toLowerCase())));
        Assert.assertTrue(this.stationEntities.stream().anyMatch(c -> c.getCountry().toLowerCase().equals(("Afghanistan").toLowerCase())));
        Assert.assertTrue(this.stationEntities.stream().anyMatch(c -> c.getCountry().toLowerCase().equals(("El Salvador").toLowerCase())));
        Assert.assertTrue(this.stationEntities.stream().anyMatch(c -> c.getCountry().toLowerCase().equals(("United Arab Emirates").toLowerCase())));
        Assert.assertTrue(this.stationEntities.stream().anyMatch(c -> c.getCountry().toLowerCase().equals(("Hong Kong").toLowerCase())));
        Assert.assertTrue(this.stationEntities.stream().anyMatch(c -> c.getCountry().toLowerCase().equals(("Côte D'Ivoire").toLowerCase())));
        Assert.assertTrue(this.stationEntities.stream().anyMatch(c -> c.getCountry().toLowerCase().equals(("South Korea").toLowerCase())));
        Assert.assertTrue(this.stationEntities.stream().anyMatch(c -> c.getCountry().toLowerCase().equals(("Macao").toLowerCase())));
        Assert.assertTrue(this.stationEntities.stream().anyMatch(c -> c.getCountry().toLowerCase().equals(("Myanmar").toLowerCase())));
        Assert.assertTrue(this.stationEntities.stream().anyMatch(c -> c.getCountry().toLowerCase().equals(("Netherlands").toLowerCase())));
        Assert.assertTrue(this.stationEntities.stream().anyMatch(c -> c.getCountry().toLowerCase().equals(("Czech Republic").toLowerCase())));
        Assert.assertTrue(this.stationEntities.stream().anyMatch(c -> c.getCountry().toLowerCase().equals(("Bosnia And Herzegovina").toLowerCase())));


        Assert.assertTrue(this.stationEntities.stream().anyMatch(c -> c.getSubdivision1() != null && c.getSubdivision1().toLowerCase().equals(("Henan").toLowerCase())));
        Assert.assertTrue(this.stationEntities.stream().anyMatch(c -> c.getSubdivision1() != null && c.getSubdivision1().toLowerCase().equals(("Massachusetts").toLowerCase())));

        Assert.assertTrue(this.stationEntities.stream().anyMatch(c -> c.getStationName() != null && c.getStationName().toLowerCase().equals(("Tel Aviv Yafo").toLowerCase())));
        Assert.assertTrue(this.stationEntities.stream().anyMatch(c -> c.getStationName() != null && c.getStationName().toLowerCase().equals(("Sankt Gallen").toLowerCase())));
        Assert.assertTrue(this.stationEntities.stream().anyMatch(c -> c.getStationName() != null && c.getStationName().toLowerCase().equals(("Los Angeles").toLowerCase())));


        Assert.assertFalse(this.stationEntities.stream().anyMatch(c -> c.getSubdivision1() != null && c.getSubdivision1().contains("-")));
        Assert.assertFalse(this.stationEntities.stream().anyMatch(c -> c.getStationName() != null && c.getStationName().contains("-")));
    }
}