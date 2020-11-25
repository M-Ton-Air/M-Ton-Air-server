package com.polytech.mtonairserver.service.interfaces;

import com.polytech.mtonairserver.customexceptions.datareader.NoProperLocationFoundException;
import com.polytech.mtonairserver.customexceptions.datareader.UnsupportedFindOperationOnLocationException;
import com.polytech.mtonairserver.customexceptions.stations.StationsAlreadyInitializedException;
import com.polytech.mtonairserver.model.entities.StationEntity;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface IStationService
{
    public List<StationEntity> findAll();

    public StationEntity findStationById(int idStation);

    public List<StationEntity> findAllByCountry(String country);

    public List<StationEntity> findAllByIso2(String iso2);

    public List<StationEntity> findAllByStationName(String stationName);

    public List<StationEntity> findAllBySubdivision(String subdivision);

    public List<StationEntity> findAllByStationNameAndCountryAndSubdivision(String any);

    public StationEntity findByUrl(String url);

    public boolean existsByStationName(String stationName);

    public void saveAllStationsToDatabaseFromFiles() throws StationsAlreadyInitializedException, NoProperLocationFoundException, UnsupportedFindOperationOnLocationException, IOException, ExecutionException, InterruptedException;

    public void deleteAll();
}
