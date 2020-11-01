package com.polytech.mtonairserver.utils.io;

import com.polytech.mtonairserver.customexceptions.datareader.NoProperLocationFoundException;
import com.polytech.mtonairserver.customexceptions.datareader.UnsupportedFindOperationOnLocationException;
import com.polytech.mtonairserver.model.entities.StationEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureTestDatabase
public class DataReaderParticularCaseHandlerTest
{

    @Autowired
    private DataReader dr;

    @Test
    public void executeAllCleaningMethods() throws IOException, NoProperLocationFoundException, UnsupportedFindOperationOnLocationException
    {
        List<StationEntity> se = dr.retrieveAllStationNames();

        Assert.assertTrue(se.stream().anyMatch(c -> c.getCountry().toLowerCase().equals(("Georgia").toLowerCase())));
        Assert.assertTrue(se.stream().anyMatch(c -> c.getCountry().toLowerCase().equals(("Afghanistan").toLowerCase())));
        Assert.assertTrue(se.stream().anyMatch(c -> c.getCountry().toLowerCase().equals(("El Salvador").toLowerCase())));
        Assert.assertTrue(se.stream().anyMatch(c -> c.getCountry().toLowerCase().equals(("United Arab Emirates").toLowerCase())));
        Assert.assertTrue(se.stream().anyMatch(c -> c.getCountry().toLowerCase().equals(("Hong Kong").toLowerCase())));
        Assert.assertTrue(se.stream().anyMatch(c -> c.getCountry().toLowerCase().equals(("Czechia").toLowerCase())));
        Assert.assertTrue(se.stream().anyMatch(c -> c.getCountry().toLowerCase().equals(("South Korea").toLowerCase())));

        Assert.assertTrue(se.stream().anyMatch(c -> c.getSubdivision1() != null && c.getSubdivision1().toLowerCase().equals(("Henan").toLowerCase())));
        Assert.assertTrue(se.stream().anyMatch(c -> c.getSubdivision1() != null && c.getSubdivision1().toLowerCase().equals(("Massachusetts").toLowerCase())));

        Assert.assertTrue(se.stream().anyMatch(c -> c.getStationName() != null && c.getStationName().toLowerCase().equals(("Tel Aviv Yafo").toLowerCase())));
        Assert.assertTrue(se.stream().anyMatch(c -> c.getStationName() != null && c.getStationName().toLowerCase().equals(("Sankt Gallen").toLowerCase())));
        Assert.assertTrue(se.stream().anyMatch(c -> c.getStationName() != null && c.getStationName().toLowerCase().equals(("Los Angeles").toLowerCase())));


        Assert.assertFalse(se.stream().anyMatch(c -> c.getSubdivision1() != null && c.getSubdivision1().contains("-")));
        Assert.assertFalse(se.stream().anyMatch(c -> c.getStationName() != null && c.getStationName().contains("-")));


    }
}