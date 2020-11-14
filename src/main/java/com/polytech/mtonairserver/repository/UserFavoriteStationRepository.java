package com.polytech.mtonairserver.repository;

import com.polytech.mtonairserver.model.entities.UserFavoriteStationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFavoriteStationRepository extends JpaRepository<UserFavoriteStationEntity, Integer> {

    public void deleteAllByIdUser(int idUser);

    public void deleteAllByIdUserAndIdStation(int idUser, int idStation);

}
