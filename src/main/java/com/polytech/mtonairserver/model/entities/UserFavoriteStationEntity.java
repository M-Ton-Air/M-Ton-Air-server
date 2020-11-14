package com.polytech.mtonairserver.model.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user_favorite_station", schema = "mtonairserver", catalog = "")
public class UserFavoriteStationEntity {
    private int idUserFavoriteStation;
    private int idUser;
    private int idStation;

    @Id
    @Column(name = "id_user_favorite_station", nullable = false)
    public int getIdUserFavoriteStation() {
        return idUserFavoriteStation;
    }

    public void setIdUserFavoriteStation(int idUserFavoriteStation) {
        this.idUserFavoriteStation = idUserFavoriteStation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserFavoriteStationEntity that = (UserFavoriteStationEntity) o;
        return idUserFavoriteStation == that.idUserFavoriteStation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUserFavoriteStation);
    }

    @Basic
    @Column(name = "id_user", nullable = false)
    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    @Basic
    @Column(name = "id_station", nullable = false)
    public int getIdStation() {
        return idStation;
    }

    public void setIdStation(int idStation) {
        this.idStation = idStation;
    }
}
