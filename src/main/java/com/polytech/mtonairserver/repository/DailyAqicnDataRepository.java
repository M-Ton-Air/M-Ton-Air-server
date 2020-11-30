package com.polytech.mtonairserver.repository;

import com.polytech.mtonairserver.model.entities.DailyAqicnDataEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface DailyAqicnDataRepository extends JpaRepository<DailyAqicnDataEntity, Integer> {
    public List<DailyAqicnDataEntity> findAllByIdStation(int idStation);


    @Query(value = "DELETE d FROM daily_aqicn_data d JOIN (SELECT max(datetime_data) AS maxdate from daily_aqicn_data) x" +
            "ON d.datetime_data < x.maxdate where d.id_station = :idStation", nativeQuery = true)
    public void deleteAllOldAqicnDatas(int idStation);

    @Query(value = "DELETE f FROM forecast f JOIN (SELECT max(date_forecasted) AS maxdate from forecast) x" +
            "ON f.date_forecasted < x.maxdate where f.id_station = :idStation", nativeQuery = true)
    public void deleteAllOldForecasts(int idStation);

    public List<DailyAqicnDataEntity> findAllByIdStationIn(Collection<Integer> ids);

}
