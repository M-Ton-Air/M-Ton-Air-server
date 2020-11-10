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
    private Double airQuality;
    private Double pm25;
    private Double o3;
    private Double pressure;
    private Double humidity;
    private Double wind;
    private Double pm10;
    private Double no2;
    private Double temperature;
    private StationEntity stationByIdStation;
    private String dominentMeasure;

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
    public Double getAirQuality() {
        return airQuality;
    }

    public void setAirQuality(Double airQuality) {
        this.airQuality = airQuality;
    }

    @Basic
    @Column(name = "pm2_5", nullable = true, precision = 0)
    public Double getPm25() {
        return pm25;
    }

    public void setPm25(Double pm25) {
        this.pm25 = pm25;
    }

    @Basic
    @Column(name = "o3", nullable = true, precision = 0)
    public Double getO3() {
        return o3;
    }

    public void setO3(Double o3) {
        this.o3 = o3;
    }

    @Basic
    @Column(name = "pressure", nullable = true, precision = 0)
    public Double getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    @Basic
    @Column(name = "humidity", nullable = true, precision = 0)
    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    @Basic
    @Column(name = "wind", nullable = true, precision = 0)
    public Double getWind() {
        return wind;
    }

    public void setWind(Double wind) {
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

    //@ManyToOne(targetEntity = StationEntity.class)
    //@JoinTable(name = "station")
    @ManyToOne
    @JoinColumn(name = "id_station", updatable = false, insertable = false)
    public StationEntity getStationByIdStation() {
        return stationByIdStation;
    }

    public void setStationByIdStation(StationEntity stationByIdStation) {
        this.stationByIdStation = stationByIdStation;
    }

    @Basic
    @Column(name = "pm10", nullable = true, precision = 0)
    public Double getPm10() {
        return pm10;
    }

    public void setPm10(Double pm10) {
        this.pm10 = pm10;
    }

    @Basic
    @Column(name = "no2", nullable = true, precision = 0)
    public Double getNo2() {
        return no2;
    }

    public void setNo2(Double no2) {
        this.no2 = no2;
    }

    @Basic
    @Column(name = "temperature", nullable = true, precision = 0)
    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    @Basic
    @Column(name = "dominent_measure", nullable = true, length = 25)
    public String getDominentMeasure() {
        return dominentMeasure;
    }

    public void setDominentMeasure(String dominentMeasure) {
        this.dominentMeasure = dominentMeasure;
    }
}