package com.polytech.mtonairserver.repository;

import com.polytech.mtonairserver.model.entities.StationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationRepository extends JpaRepository<StationEntity, Integer>
{
    public List<StationEntity> findAll();

    public StationEntity findByIdStation(int idStation);

    @Query("SELECT s FROM StationEntity s WHERE s.country LIKE %:country%")
    public List<StationEntity> findAllByCountry(
            @Param("country") String country);

    public List<StationEntity> findAllByIso2(String iso2);

    @Query("SELECT s FROM StationEntity s WHERE s.stationName LIKE %:stationName%")
    public List<StationEntity> findAllByStationName(String stationName);

    public List<StationEntity> findAllBySubdivision1(String subdivision1);

    public List<StationEntity> findAllBySubdivision2(String subdivision1);

    public List<StationEntity> findAllBySubdivision3(String subdivision1);

    @Query("SELECT s FROM StationEntity s WHERE s.subdivision1 LIKE %:subdivision% " +
            "OR s.subdivision2 LIKE %:subdivision% " +
            "OR s.subdivision3 LIKE %:subdivision%")
    public List<StationEntity> findAllBySubdivision(String subdivision);

    public StationEntity findAllByUrl(String url);

    public boolean existsByStationName(String stationName);

}
