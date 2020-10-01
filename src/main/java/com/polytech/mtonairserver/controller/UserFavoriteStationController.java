package com.polytech.mtonairserver.controller;

import com.polytech.mtonairserver.domains.UserFavoriteStationEntity;
import com.polytech.mtonairserver.repositories.UserFavoriteStationEntityRepository;
import io.swagger.annotations.ApiOperation;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserFavoriteStationController {

    private UserFavoriteStationEntityRepository userFavoriteStationEntityRepository;

    @Autowired
    public UserFavoriteStationController(UserFavoriteStationEntityRepository userFavoriteStationEntityRepository) {
        this.userFavoriteStationEntityRepository = userFavoriteStationEntityRepository;
    }


    /**
     * Retrieve the list of favorite stations
     * @return favorite stations list
     */
    @RequestMapping(value = "/favoriteStations", method= RequestMethod.GET)
    public List<Object[]> findAllUsersFavoriteStation() {
        return userFavoriteStationEntityRepository.listerStationsFavUsers();
    }

    /**
     * Retrieve user's favorite station(s) by his id
     * @param id user's id
     * @return user's favorite station(s)
     */
    @RequestMapping(value = "/favoriteStations/{id}", method = RequestMethod.GET)
    public List<Object[]> displayUsersFavoriteStations(@PathVariable int id) {
        return userFavoriteStationEntityRepository.findByIdUser(id);
    }
}
