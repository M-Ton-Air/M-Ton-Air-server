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

import java.util.List;
import java.util.Set;

/**
 * Users controller.
 */
@RestController
@Api(tags = SwaggerConfig.USERS_NAME_TAG)
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

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
    public ResponseEntity<List<UserEntity>> listOfUsers() {
        return new ResponseEntity<List<UserEntity>>(this.userService.findAllUsersWithoutTheirFavoriteStations(), HttpStatus.OK);
    }

    /**
     * Retrieve a user by his id
     * @param id users id
     * @return a specific user
     */
    @ApiOperation(value = "Gets a user with his id", notes = "Retrieves a user according to a given id")
    @RequestMapping(value = "/{id}", method= RequestMethod.GET)
    public ResponseEntity<UserEntity> getAUser(
            @ApiParam(name = "id", value = "The user id", required = true)
            @PathVariable int id) {
        return new ResponseEntity<UserEntity>(this.userService.findByIdUser(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Gets the favorite stations for a given user", notes = "Returns 0, one or many stations" +
            " according to the user favorite stations.")
    @RequestMapping(value = "/{id}/favorite-stations", method = RequestMethod.GET)
    public ResponseEntity<Set<StationEntity>> getUserFavoriteStations(
            @ApiParam(name = "id", value = "The user id", required = true)
            @PathVariable int id) throws UserFavoriteStationsFetchException
    {
        try
        {
            return new ResponseEntity<Set<StationEntity>>(this.userService.listUserFavoriteStations(id), HttpStatus.OK);
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
        ex.setStackTrace(new StackTraceElement[]{ex.getStackTrace()[0]});
        return new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Could not fetch the user favorite stations. Server side error occured.", ex);
    }


}
