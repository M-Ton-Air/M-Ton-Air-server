package com.polytech.mtonairserver.repository;

import com.polytech.mtonairserver.model.entities.StationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationRepository extends JpaRepository<StationEntity, Integer>
{
    public List<StationEntity> findAll();

    public List<StationEntity> findAllByCountry(String country);

    public List<StationEntity> findAllByIso2(String iso2);

    public List<StationEntity> findAllByStationName(String stationName);

    public List<StationEntity> findAllBySubdivision1(String subdivision1);

    public List<StationEntity> findAllBySubdivision2(String subdivision1);

    public List<StationEntity> findAllBySubdivision3(String subdivision1);

    public boolean existsByStationName(String stationName);

}
