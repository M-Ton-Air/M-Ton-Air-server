package com.polytech.mtonairserver.model.entities;

import com.polytech.mtonairserver.model.entities.keys.DailyAqicnDataEntityPK;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "daily_aqicn_data", schema = "mtonairserver", catalog = "")
@IdClass(DailyAqicnDataEntityPK.class)
public class DailyAqicnDataEntity  {
    private int idDailyAqicnData;
    private int idStation;
    private Timestamp datetimeData;
    private double airQuality;
    private double pm25;
    private double o3;
    private double pressure;
    private double humidity;
    private double wind;
    private StationEntity stationByIdStation;

    @Id
    @Column(name = "id_daily_aqicn_data", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getIdDailyAqicnData() {
        return idDailyAqicnData;
    }

    public void setIdDailyAqicnData(int idDailyAqicnData) {
        this.idDailyAqicnData = idDailyAqicnData;
    }

    @Id
    @Column(name = "id_station", nullable = false, insertable = false, updatable = false)
    public int getIdStation() {
        return idStation;
    }

    public void setIdStation(int idStation) {
        this.idStation = idStation;
    }

    @Id
    @Column(name = "datetime_data", nullable = false)
    public Timestamp getDatetimeData() {
        return datetimeData;
    }

    public void setDatetimeData(Timestamp datetimeData) {
        this.datetimeData = datetimeData;
    }

    @Basic
    @Column(name = "air_quality", nullable = false, precision = 0)
    public double getAirQuality() {
        return airQuality;
    }

    public void setAirQuality(double airQuality) {
        this.airQuality = airQuality;
    }

    @Basic
    @Column(name = "pm2_5", nullable = false, precision = 0)
    public double getPm25() {
        return pm25;
    }

    public void setPm25(double pm25) {
        this.pm25 = pm25;
    }

    @Basic
    @Column(name = "o3", nullable = false, precision = 0)
    public double getO3() {
        return o3;
    }

    public void setO3(double o3) {
        this.o3 = o3;
    }

    @Basic
    @Column(name = "pressure", nullable = false, precision = 0)
    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    @Basic
    @Column(name = "humidity", nullable = false, precision = 0)
    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    @Basic
    @Column(name = "wind", nullable = false, precision = 0)
    public double getWind() {
        return wind;
    }

    public void setWind(double wind) {
        this.wind = wind;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DailyAqicnDataEntity that = (DailyAqicnDataEntity) o;
        return idDailyAqicnData == that.idDailyAqicnData &&
                idStation == that.idStation &&
                Double.compare(that.airQuality, airQuality) == 0 &&
                Double.compare(that.pm25, pm25) == 0 &&
                Double.compare(that.o3, o3) == 0 &&
                Double.compare(that.pressure, pressure) == 0 &&
                Double.compare(that.humidity, humidity) == 0 &&
                Double.compare(that.wind, wind) == 0 &&
                Objects.equals(datetimeData, that.datetimeData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDailyAqicnData, idStation, datetimeData, airQuality, pm25, o3, pressure, humidity, wind);
    }

    @ManyToOne
    @JoinColumn(name = "id_station", referencedColumnName = "id_station", nullable = false)
    public StationEntity getStationByIdStation() {
        return stationByIdStation;
    }

    public void setStationByIdStation(StationEntity stationByIdStation) {
        this.stationByIdStation = stationByIdStation;
    }
}