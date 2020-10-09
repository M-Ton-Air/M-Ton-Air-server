package com.polytech.mtonairserver.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "favorite_station", schema = "mtonairserver", catalog = "")
public class FavoriteStationEntity {
    private int idFavoriteStation;
    private String stationName;
    private String url;

    @Id
    @Column(name = "id_favorite_station")
    public int getIdFavoriteStation() {
        return idFavoriteStation;
    }

    public void setIdFavoriteStation(int idFavoriteStation) {
        this.idFavoriteStation = idFavoriteStation;
    }

    @Basic
    @Column(name = "station_name")
    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    @Basic
    @Column(name = "url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FavoriteStationEntity that = (FavoriteStationEntity) o;
        return idFavoriteStation == that.idFavoriteStation &&
                Objects.equals(stationName, that.stationName) &&
                Objects.equals(url, that.url);
    }

    @Override
    public String toString()
    {
        return "FavoriteStationEntity{" +
                "idFavoriteStation=" + idFavoriteStation +
                ", stationName='" + stationName + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(idFavoriteStation, stationName, url);
    }
}
