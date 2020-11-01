package com.polytech.mtonairserver.repository;

import com.polytech.mtonairserver.model.entities.DailyAqicnDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DailyAqicnDataRepository extends JpaRepository<DailyAqicnDataEntity, Integer> {

    public List<DailyAqicnDataEntity> findByIdStation(int idStation);

}
