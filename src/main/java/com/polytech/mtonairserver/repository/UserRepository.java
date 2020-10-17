package com.polytech.mtonairserver.repository;

import com.polytech.mtonairserver.model.entities.StationEntity;
import com.polytech.mtonairserver.model.entities.UserEntity;
import com.polytech.mtonairserver.model.entities.UserFavoriteStationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * Public repository operations for the users entities.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    public UserEntity findByEmail(String email);

    public boolean existsByEmail(String email);

    public boolean existsByApiKey(String email);

    /**
     * Selects all the favorite stations for a given user (by id).
     * @param idUser the concerned user.
     * @return The list of favorite stations for a given user.
     */
    @Query("SELECT S.idStation, S.stationName, S.url, S.country, S.region, S.city " +
            "FROM StationEntity S, UserFavoriteStationEntity UFS " +
            "WHERE UFS.idUser = :idUser")
    public Collection<StationEntity> listUserFavoriteStations(@Param("idUser") int idUser);

}
