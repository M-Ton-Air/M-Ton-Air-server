package com.polytech.mtonairserver.service.interfaces;

import com.polytech.mtonairserver.customexceptions.favoritestation.StationIsNotIntoUserFavoriteStationsException;
import com.polytech.mtonairserver.customexceptions.favoritestation.StationAlreadyInUserFavoriteStationsException;
import com.polytech.mtonairserver.customexceptions.stations.StationDoesntExistIntoTheDatabaseException;
import com.polytech.mtonairserver.model.entities.StationEntity;

import java.util.Set;

public interface IUserFavoriteStationService {

    Set<StationEntity> listUserFavoriteStations(int userId);

    void addUserFavoriteStation(int idUser, int idStation) throws StationAlreadyInUserFavoriteStationsException, StationDoesntExistIntoTheDatabaseException;

    void deleteAllUserFavoriteStations(int idUser);

    void deleteSpecificUserFavoriteStation(int idUser, int idStation) throws StationIsNotIntoUserFavoriteStationsException;

}
