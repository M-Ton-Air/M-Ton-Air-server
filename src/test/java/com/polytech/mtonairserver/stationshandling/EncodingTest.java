package com.polytech.mtonairserver.stationshandling;

import com.polytech.mtonairserver.model.entities.StationEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureTestDatabase
public class EncodingTest extends StationsDataReaderTest
{
    @Test
    public void testEncoding()
    {
        for(StationEntity station : StationsDataReaderTest.stationEntities)
        {
            Assert.assertFalse(station.getCountry().contains("?"));
            Assert.assertFalse(station.getSubdivision1() != null && station.getSubdivision1().contains("?"));
            Assert.assertFalse(station.getSubdivision2() != null && station.getSubdivision1().contains("?"));
            Assert.assertFalse(station.getSubdivision3() != null && station.getSubdivision1().contains("?"));
            Assert.assertFalse(station.getStationName().contains("?"));
        }
    }
}
