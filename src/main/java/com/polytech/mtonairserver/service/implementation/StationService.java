package com.polytech.mtonairserver.service.implementation;

import com.polytech.mtonairserver.customexceptions.datareader.NoProperLocationFoundException;
import com.polytech.mtonairserver.customexceptions.datareader.UnsupportedFindOperationOnLocationException;
import com.polytech.mtonairserver.customexceptions.stations.StationsAlreadyInitializedException;
import com.polytech.mtonairserver.model.entities.StationEntity;
import com.polytech.mtonairserver.repository.StationRepository;
import com.polytech.mtonairserver.service.interfaces.IStationService;

import com.polytech.mtonairserver.stationshandling.io.DataReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * Handling station retrieval and station creation thanks to the DataReader.
 */
@Service
public class StationService implements IStationService
{
    private final StationRepository stationRepository;

    private final DataReader dataReader;

    @Autowired
    public StationService(StationRepository _stationRepository, DataReader dr)
    {
        this.stationRepository = _stationRepository;
        this.dataReader = dr;
    }

    private static String hostLinkRealTimeAQI = "http://aqicn.org/city/";

    public static String getHostLinkRealTimeAQI()
    {
        return hostLinkRealTimeAQI;
    }

    public static void setHostLinkRealTimeAQI(String hostLinkRealTimeAQI)
    {
        StationService.hostLinkRealTimeAQI = hostLinkRealTimeAQI;
    }

    @Override
    public List<StationEntity> findAll()
    {
        return this.stationRepository.findAll();
    }

    @Override
    public List<StationEntity> findAllByCountry(String country)
    {
        return this.stationRepository.findAllByCountry(country);
    }

    @Override
    public List<StationEntity> findAllByIso2(String iso2)
    {
        return this.stationRepository.findAllByIso2(iso2);
    }


    @Override
    public List<StationEntity> findAllByStationName(String stationName)
    {
        return this.stationRepository.findAllByStationName(stationName);
    }

    @Override
    public List<StationEntity> findAllBySubdivision(String subdivision)
    {
        List<StationEntity> result = this.stationRepository.findAllBySubdivision1(subdivision);
        result.addAll(               this.stationRepository.findAllBySubdivision2(subdivision));
        result.addAll(               this.stationRepository.findAllBySubdivision3(subdivision));

        return result;
    }

    @Override
    public boolean existsByStationName(String city)
    {
        return this.stationRepository.existsByStationName(city);
    }

    @Override
    public void saveAllStationsToDatabaseFromFiles() throws StationsAlreadyInitializedException, NoProperLocationFoundException, UnsupportedFindOperationOnLocationException, IOException, ExecutionException, InterruptedException
    {
        if(this.stationRepository.count() > 0)
        {
            throw new StationsAlreadyInitializedException("There are already existing stations in the current DB.", StationService.class);
        }
        else
        {
            this.stationRepository.saveAll(this.dataReader.initializeAllStationsFromAqicnStationsHtmlFile());
        }
    }

    @Override
    public void deleteAll()
    {
        this.stationRepository.deleteAll();
    }
}