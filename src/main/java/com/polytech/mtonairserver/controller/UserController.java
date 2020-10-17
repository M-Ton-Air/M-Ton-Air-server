package com.polytech.mtonairserver.controller;

import com.polytech.mtonairserver.config.SwaggerConfig;
import com.polytech.mtonairserver.customexceptions.miscellaneous.UserFavoriteStationsFetchException;
import com.polytech.mtonairserver.model.entities.StationEntity;
import com.polytech.mtonairserver.model.entities.UserEntity;
import com.polytech.mtonairserver.model.responses.ApiErrorResponse;
import com.polytech.mtonairserver.service.implementation.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

/**
 * Users controller.
 */
@RestController
@Api(tags = SwaggerConfig.USERS_NAME_TAG)
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService _userService) {
        this.userService = _userService;
    }

    // todo : restrict the user api usage, otherwise anyone can access these data.

    /**
     * Retrieve the users list
     * @return users list
     */
    @ApiOperation(value = "Get the users list", notes = "gets all the" +
            "available users stored in the M-Ton-Air database.")
    @RequestMapping(method= RequestMethod.GET)
    public List<UserEntity> listOfUsers() {
        return this.userService.findAll();
    }

    /**
     * Retrieve a user by his id
     * @param id users id
     * @return a specific user
     */
    //todo : documenter les autres méthodes de cette manière
    @ApiOperation(value = "Gets a user with his id", notes = "Retrieves a user according to a given id")
    @RequestMapping(value = "/{id}", method= RequestMethod.GET)
    public UserEntity getAUser(
            @ApiParam(name = "id", value = "The user id", required = true)
            @PathVariable int id) {
       // return userDao.findById(id);
        return this.userService.findByIdUser(id);
    }

    @ApiOperation(value = "Gets the favorite stations of a given user", notes = "Returns 0, one or many stations" +
            " according to the user favorite stations.")
    @RequestMapping(value = "/{id}/favoriteStations", method = RequestMethod.GET)
    public Collection<StationEntity> getUserFavoriteStations(
            @ApiParam(name = "id", value = "The user id", required = true)
            @PathVariable int id) throws UserFavoriteStationsFetchException
    {
        //Todo : not working
        /**
         *         "message": "Failed to convert from type [java.lang.Object[]] to
         *         type [com.polytech.mtonairserver.model.entities.StationEntity]
         *         for value '{1, MaStation, /url/test, France, Rhône, Lyon}';
         *         nested exception is org.springframework.core.convert.ConverterNotFoundException:
         *         No converter found capable of converting from type [java.lang.Integer] to type
         *         [com.polytech.mtonairserver.model.entities.StationEntity]",
         */
        try
        {
            return this.userService.listUserFavoriteStations(id);
        }
        catch(Exception e)
        {
            throw new UserFavoriteStationsFetchException(e.getMessage(), UserController.class);
        }
    }


    //todo : add a favorite station.


    /* ############################################################## EXCEPTION HANDLERS ############################################################## */

    /**
     * Custom Exception Handler for invalid variables length.
     * @param ex an InvalidVariablesLengthException.
     * @return an ApiErrorResponse describing the error.
     */
    @ExceptionHandler(UserFavoriteStationsFetchException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorResponse invalidVarLength(UserFavoriteStationsFetchException ex)
    {
        // ignores unuseful elements
        ex.setStackTrace(new StackTraceElement[]{ex.getStackTrace()[0]});
        return new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Could not fetch the user favorite stations. Server side error occured.", ex);
    }


}
