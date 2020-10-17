package com.polytech.mtonairserver.model.entities;


import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user_favorite_station", schema = "mtonairserver")
public class UserFavoriteStationEntity {
    private int idUserFavoriteStation;
    private int idUser;
    private int idStation;

    public UserFavoriteStationEntity() { }

    @Id
    @Column(name = "id_user_favorite_station")
    public int getIdUserFavoriteStation() {
        return idUserFavoriteStation;
    }

    @Basic
    @Column(name = "id_user")
    public int getIdUser() {
        return idUser;
    }

    @Basic
    @Column(name = "id_station")
    public int getIdStation() {
        return idStation;
    }

    public void setIdUserFavoriteStation(int idUserFavoriteStation)
    {
        this.idUserFavoriteStation = idUserFavoriteStation;
    }

    public void setIdUser(int idUser)
    {
        this.idUser = idUser;
    }

    public void setIdStation(int idStation)
    {
        this.idStation = idStation;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserFavoriteStationEntity that = (UserFavoriteStationEntity) o;
        return idUserFavoriteStation == that.idUserFavoriteStation &&
                idUser == that.idUser &&
                idStation == that.idStation;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(idUserFavoriteStation, idUser, idStation);
    }
}
