package com.polytech.mtonairserver.model.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "station", schema = "mtonairserver", catalog = "")
public class StationEntity
{
    private int idStation;
    private String stationName;
    private String url;

    @Id
    @Column(name = "id_station", nullable = false)
    public int getIdStation()
    {
        return idStation;
    }

    public void setIdStation(int idStation)
    {
        this.idStation = idStation;
    }

    @Basic
    @Column(name = "station_name", nullable = false, length = 300)
    public String getStationName()
    {
        return stationName;
    }

    public void setStationName(String stationName)
    {
        this.stationName = stationName;
    }

    @Basic
    @Column(name = "url", nullable = false, length = 300)
    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StationEntity that = (StationEntity) o;
        return idStation == that.idStation &&
                Objects.equals(stationName, that.stationName) &&
                Objects.equals(url, that.url);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(idStation, stationName, url);
    }
}
