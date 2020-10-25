package com.polytech.mtonairserver.model.entities.keys;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

public class ForecastEntityPK implements Serializable {
    private int idForecast;
    private int idStation;
    private int idMeasure;
    private Date idDateForecast;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_forecast", nullable = false)
    @Id
    public int getIdForecast() {
        return idForecast;
    }

    public void setIdForecast(int idForecast) {
        this.idForecast = idForecast;
    }

    @Column(name = "id_station", nullable = false, insertable = false, updatable = false)
    @Id
    public int getIdStation() {
        return idStation;
    }

    public void setIdStation(int idStation) {
        this.idStation = idStation;
    }

    @Column(name = "id_measure", nullable = false, insertable = false, updatable = false)
    @Id
    public int getIdMeasure() {
        return idMeasure;
    }

    public void setIdMeasure(int idMeasure) {
        this.idMeasure = idMeasure;
    }

    @Column(name = "id_date_forecast", nullable = false)
    @Id
    public Date getIdDateForecast() {
        return idDateForecast;
    }

    public void setIdDateForecast(Date idDateForecast) {
        this.idDateForecast = idDateForecast;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ForecastEntityPK that = (ForecastEntityPK) o;
        return idForecast == that.idForecast &&
                idStation == that.idStation &&
                idMeasure == that.idMeasure &&
                Objects.equals(idDateForecast, that.idDateForecast);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idForecast, idStation, idMeasure, idDateForecast);
    }
}
