package com.polytech.mtonairserver.model.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

public class DailyAqicnDataEntityPK implements Serializable {
    private int idDailyAqicnData;
    private int idStation;
    private Timestamp datetimeData;

    @Column(name = "id_daily_aqicn_data", nullable = false)
    @Id
    public int getIdDailyAqicnData() {
        return idDailyAqicnData;
    }

    public void setIdDailyAqicnData(int idDailyAqicnData) {
        this.idDailyAqicnData = idDailyAqicnData;
    }

    @Column(name = "id_station", nullable = false)
    @Id
    public int getIdStation() {
        return idStation;
    }

    public void setIdStation(int idStation) {
        this.idStation = idStation;
    }

    @Column(name = "datetime_data", nullable = false)
    @Id
    public Timestamp getDatetimeData() {
        return datetimeData;
    }

    public void setDatetimeData(Timestamp datetimeData) {
        this.datetimeData = datetimeData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DailyAqicnDataEntityPK that = (DailyAqicnDataEntityPK) o;
        return idDailyAqicnData == that.idDailyAqicnData &&
                idStation == that.idStation &&
                Objects.equals(datetimeData, that.datetimeData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDailyAqicnData, idStation, datetimeData);
    }
}
