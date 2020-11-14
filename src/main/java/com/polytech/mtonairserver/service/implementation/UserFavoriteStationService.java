package com.polytech.mtonairserver.service.implementation;

import com.polytech.mtonairserver.customexceptions.favoritestation.StationIsNotIntoUserFavoriteStationsException;
import com.polytech.mtonairserver.customexceptions.favoritestation.StationAlreadyInUserFavoriteStationsException;
import com.polytech.mtonairserver.customexceptions.stations.StationDoesntExistIntoTheDatabaseException;
import com.polytech.mtonairserver.model.entities.StationEntity;
import com.polytech.mtonairserver.model.entities.UserEntity;
import com.polytech.mtonairserver.repository.StationRepository;
import com.polytech.mtonairserver.repository.UserFavoriteStationRepository;
import com.polytech.mtonairserver.repository.UserRepository;
import com.polytech.mtonairserver.service.interfaces.IUserFavoriteStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserFavoriteStationService implements IUserFavoriteStationService {

    private UserFavoriteStationRepository userFavoriteStationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    public UserFavoriteStationService(UserFavoriteStationRepository userFavoriteStationRepository) {
        this.userFavoriteStationRepository = userFavoriteStationRepository;
    }

    @Override
    public Set<StationEntity> listUserFavoriteStations(int userId)
    {
        UserEntity ue = this.userRepository.findAllByIdUser(userId);
        return ue.getUserFavoriteStationsByIdUser();
    }

    @Override
    public void addUserFavoriteStation(int idUser, int idStation) throws StationAlreadyInUserFavoriteStationsException, StationDoesntExistIntoTheDatabaseException {

        boolean isStationExistingIntoUserFavoriteStation = false;

        UserEntity userEntity = this.userRepository.findAllByIdUser(idUser);
        Set<StationEntity> stations = userEntity.getUserFavoriteStationsByIdUser();

        StationEntity stationEntityList = this.stationRepository.findByIdStation(idStation);

        for (StationEntity station : stations) {
            if (station.getIdStation() == idStation) {
                isStationExistingIntoUserFavoriteStation = true;
            }
        }

        if (!isStationExistingIntoUserFavoriteStation) {
            if (this.stationRepository.findByIdStation(idStation) != null) {
                StationEntity stationEntity = this.stationRepository.findByIdStation(idStation);
                stations.add(stationEntity);
                userEntity.setUserFavoriteStationsByIdUser(stations);
                this.userRepository.save(userEntity);
            }
            else {
                throw new StationAlreadyInUserFavoriteStationsException("The station n°" + idStation + " doesn't exists " +
                        "into the M'Ton'Air Database", UserFavoriteStationService.class);
            }

        }
        else {

            throw new StationDoesntExistIntoTheDatabaseException("The station n°" + idStation + " is already into "
                    + userEntity.getEmail() + " favorite station", UserFavoriteStationService.class);
        }
    }

    @Override
    public void deleteAllUserFavoriteStations(int idUser) {
        this.userFavoriteStationRepository.deleteAllByIdUser(idUser);
    }

    @Override
    public void deleteSpecificUserFavoriteStation(int idUser, int idStation) throws StationIsNotIntoUserFavoriteStationsException {

        boolean isThisStationInTheUsersFavoriteStations = false;

        UserEntity userEntity = this.userRepository.findAllByIdUser(idUser);
        Set<StationEntity> stationEntities = userEntity.getUserFavoriteStationsByIdUser();
        for (StationEntity stationEntity : stationEntities) {
            if (stationEntity.getIdStation() == idStation) {
                isThisStationInTheUsersFavoriteStations = true;
            }
        }
        if (isThisStationInTheUsersFavoriteStations) {
            this.userFavoriteStationRepository.deleteAllByIdUserAndIdStation(idUser, idStation);
        }
        else {
            throw new StationIsNotIntoUserFavoriteStationsException("The station n°" + idStation + " is not into user n°"
                    + idUser + " favorite station", UserFavoriteStationService.class);
        }

    }
}
