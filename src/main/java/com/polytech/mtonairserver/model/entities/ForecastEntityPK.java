package com.polytech.mtonairserver.model.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

public class ForecastEntityPK implements Serializable {
    private int idForecast;
    private int idStation;
    private Date idDateForecast;

    @Column(name = "id_forecast")
    @Id
    public int getIdForecast() {
        return idForecast;
    }

    public void setIdForecast(int idForecast) {
        this.idForecast = idForecast;
    }

    @Column(name = "id_station")
    @Id
    public int getIdStation() {
        return idStation;
    }

    public void setIdStation(int idStation) {
        this.idStation = idStation;
    }

    @Column(name = "id_date_forecast")
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
                Objects.equals(idDateForecast, that.idDateForecast);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idForecast, idStation, idDateForecast);
    }
}
