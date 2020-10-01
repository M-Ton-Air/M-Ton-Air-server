package com.polytech.mtonairserver.repositories;

import com.polytech.mtonairserver.domains.UserFavoriteStationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFavoriteStationEntityRepository extends JpaRepository<UserFavoriteStationEntity, Integer> {


    @Query("SELECT ufs.idUser, u.firstname, u.name, ufs.idFavoriteStation, fs.stationName, fs.url " +
            "FROM UserFavoriteStationEntity ufs, FavoriteStationEntity fs, UserEntity u " +
            "WHERE ufs.idFavoriteStation = fs.idFavoriteStation AND ufs.idUser = u.idUser")
    public List<Object[]> listerStationsFavUsers();

    @Query("SELECT ufs.idFavoriteStation, fs.stationName, fs.url " +
            "FROM UserFavoriteStationEntity ufs, FavoriteStationEntity fs " +
            "WHERE ufs.idFavoriteStation = fs.idFavoriteStation " +
            "AND ufs.idUser = :idUser")
    public List<Object[]> findByIdUser(@Param("idUser") int idUser);

}
