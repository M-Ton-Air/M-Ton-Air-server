package com.polytech.mtonairserver.controller;

import com.polytech.mtonairserver.config.SwaggerConfig;
import com.polytech.mtonairserver.repositories.UserFavoriteStationEntityRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller that handles the user favorite stations.
 */
@RestController
@Api(tags = SwaggerConfig.USERS_FAVORITE_STATIONS_NAME_TAG)
@RequestMapping("/favoriteStations")
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
    @RequestMapping(method= RequestMethod.GET)
    @ApiOperation(value = "Gets all the users favorite stations", notes = "this method has no appropriate use for any feature right now.")
    public List<Object[]> findAllUsersFavoriteStation() {
        return userFavoriteStationEntityRepository.listerStationsFavUsers();
    }

    /**
     * Retrieve user's favorite station(s) by his id
     * @param id user's id
     * @return user's favorite station(s)
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Gets a user's favorite station with a given user id.", notes = "useful to know which stations are saved for a given user.")
    public List<Object[]> getUsersFavoriteStations(@ApiParam(name = "id", value = "the user id", required = true)
                                                   @PathVariable int id) {
        return userFavoriteStationEntityRepository.findByIdUser(id);
    }
}
