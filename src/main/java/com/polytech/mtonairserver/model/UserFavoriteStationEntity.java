package com.polytech.mtonairserver.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user_favorite_station", schema = "mtonairserver", catalog = "")
public class UserFavoriteStationEntity {
    private int idUserFavoriteStation;
    private int idUser;
    private int idFavoriteStation;

    @Id
    @Column(name = "id_user_favorite_station")
    public int getIdUserFavoriteStation() {
        return idUserFavoriteStation;
    }

    public void setIdUserFavoriteStation(int idUserFavoriteStation) {
        this.idUserFavoriteStation = idUserFavoriteStation;
    }

    @Basic
    @Column(name = "id_user")
    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    @Basic
    @Column(name = "id_favorite_station")
    public int getIdFavoriteStation() {
        return idFavoriteStation;
    }

    public void setIdFavoriteStation(int idFavoriteStation) {
        this.idFavoriteStation = idFavoriteStation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserFavoriteStationEntity that = (UserFavoriteStationEntity) o;
        return idUserFavoriteStation == that.idUserFavoriteStation &&
                idUser == that.idUser &&
                idFavoriteStation == that.idFavoriteStation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUserFavoriteStation, idUser, idFavoriteStation);
    }
}
