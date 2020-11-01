package com.polytech.mtonairserver.repository;

import com.polytech.mtonairserver.model.entities.ForecastEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForecastRepository extends JpaRepository<ForecastEntity, Integer> {

    public List<ForecastEntity> findByIdStation(int idStation);

    public List<ForecastEntity> findByIdStationAndMeasureByIdMeasure_MeasureName(int idStation, String measureName);

}
