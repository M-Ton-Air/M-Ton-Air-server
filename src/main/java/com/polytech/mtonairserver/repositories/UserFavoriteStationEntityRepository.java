package com.polytech.mtonairserver.repositories;

import com.polytech.mtonairserver.model.UserFavoriteStationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Public operations for the usersfavoritestations entities.
 */
@Repository
public interface UserFavoriteStationEntityRepository extends JpaRepository<UserFavoriteStationEntity, Integer> {

    // todo : utile pour une feature ?
    @Query("SELECT ufs.idUser, u.firstname, u.name, ufs.idFavoriteStation, fs.stationName, fs.url " +
            "FROM UserFavoriteStationEntity ufs, FavoriteStationEntity fs, UserEntity u " +
            "WHERE ufs.idFavoriteStation = fs.idFavoriteStation AND ufs.idUser = u.idUser")
    public List<Object[]> listerStationsFavUsers();

    /**
     * Selects all the favorite stations for a given user (by id).
     * @param idUser the concerned user.
     * @return The list of favorite stations for a given user.
     */
    @Query("SELECT ufs.idFavoriteStation, fs.stationName, fs.url " +
            "FROM UserFavoriteStationEntity ufs, FavoriteStationEntity fs " +
            "WHERE ufs.idFavoriteStation = fs.idFavoriteStation " +
            "AND ufs.idUser = :idUser")
    public List<Object[]> findByIdUser(@Param("idUser") int idUser);

}
