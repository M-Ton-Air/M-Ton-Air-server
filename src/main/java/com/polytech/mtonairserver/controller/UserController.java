package com.polytech.mtonairserver.controller;

import com.polytech.mtonairserver.config.SwaggerConfig;
import com.polytech.mtonairserver.customexceptions.ControllerExceptionBuilder;
import com.polytech.mtonairserver.customexceptions.favoritestation.StationAlreadyInUserFavoriteStationsException;
import com.polytech.mtonairserver.customexceptions.miscellaneous.UserFavoriteStationsFetchException;
import com.polytech.mtonairserver.customexceptions.stations.StationDoesntExistIntoTheDatabaseException;
import com.polytech.mtonairserver.model.entities.DailyAqicnDataEntity;
import com.polytech.mtonairserver.model.entities.StationEntity;
import com.polytech.mtonairserver.model.entities.UserEntity;
import com.polytech.mtonairserver.model.responses.ApiErrorResponse;
import com.polytech.mtonairserver.model.responses.ApiResponse;
import com.polytech.mtonairserver.model.responses.ApiSuccessResponse;
import com.polytech.mtonairserver.service.implementation.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
    @RequestMapping(value = "/{id}/favorite-station", method = RequestMethod.GET)
    public ResponseEntity<List<DailyAqicnDataEntity>> getUserFavoriteStationsData(
            @ApiParam(name = "id", value = "The user id", required = true)
            @PathVariable int id) throws UserFavoriteStationsFetchException
    {
        try
        {
            return new ResponseEntity<List<DailyAqicnDataEntity>>(this.userService.listUserFavoriteAqicnData(id), HttpStatus.OK);
        }
        catch(Exception e)
        {
            throw new UserFavoriteStationsFetchException(e.getMessage(), UserController.class);
        }
    }


    @RequestMapping(value = "/{id}/favorite-station/stations-only", method = RequestMethod.GET)
    public ResponseEntity<List<StationEntity>> getUserFavoriteStations(
            @ApiParam(name = "id", value = "The user id", required = true)
            @PathVariable int id) throws UserFavoriteStationsFetchException
    {
        try
        {
            return new ResponseEntity<List<StationEntity>>(this.userService.getFavoriteStations(id), HttpStatus.OK);
        }
        catch(Exception e)
        {
            throw new UserFavoriteStationsFetchException(e.getMessage(), UserController.class);
        }
    }

    @ApiOperation(value = "Add a station into a specific user favorite stations", notes = "Add a user favorite station")
    @RequestMapping(value = "/{idUser}/favorite-station/{idStation}/add", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse> addAUserFavoriteStation(
            @ApiParam(name = "idUser", value = "The user id", required = true)
            @PathVariable int idUser,
            @ApiParam(name = "idStation", value = "The station id", required = true)
            @PathVariable int idStation) throws StationAlreadyInUserFavoriteStationsException, StationDoesntExistIntoTheDatabaseException {
            this.userService.addUserFavoriteStation(idUser, idStation);

        return new ResponseEntity<ApiResponse>(
                new ApiSuccessResponse(
                        HttpStatus.OK,
                        "The station n°" + idStation + " is added to user n°" + idUser + " favorite stations"),
                HttpStatus.OK
        );
    }

    @ApiOperation(value = "Delete all user favorite stations", notes = "Delete all favorite stations to a specific user")
    @RequestMapping(value = "/{idUser}/favorite-stations/delete", method = RequestMethod.DELETE)
    @Transactional
    public ResponseEntity<ApiResponse> deleteAllUserFavoriteStations(
            @ApiParam(name = "idUser", value = "The user id", required = true)
            @PathVariable int idUser) {

        try {
            this.userService.deleteAllUserFavoriteStations(idUser);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<ApiResponse>(new ApiErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Could not delete all the user n°" + idUser + " favorite stations", e),
                    HttpStatus.INTERNAL_SERVER_ERROR);

        }

        return new ResponseEntity<ApiResponse>(
                new ApiSuccessResponse(
                        HttpStatus.OK,
                        "All favorite stations of user n°" + idUser + " were deleted."),
                HttpStatus.OK
        );
    }

    @ApiOperation(value = "Delete a specific user's favorite station", notes = "Delete a  specific favorite station to a specific user")
    @RequestMapping(value = "/{idUser}/favorite-station/{idStation}/delete", method = RequestMethod.DELETE)
    @Transactional
    public ResponseEntity<ApiResponse> deleteSpecificUserFavoriteStation(
            @ApiParam(name = "idUser", value = "The user id", required = true)
            @PathVariable int idUser,
            @ApiParam(name = "idStation", value = "The station id", required = true)
            @PathVariable int idStation) {

        try {
            this.userService.deleteSpecificUserFavoriteStation(idUser, idStation);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<ApiResponse>(new ApiErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Could not delete favorite station n°" + idStation + " from user n°" + idUser, e),
                    HttpStatus.INTERNAL_SERVER_ERROR);

        }

        return new ResponseEntity<ApiResponse>(
                new ApiSuccessResponse(
                        HttpStatus.OK,
                        "The favorite station n°" + idStation + " was deleted from the user n°" + idUser),
                HttpStatus.OK
        );
    }


    /* ############################################################## EXCEPTION HANDLERS ############################################################## */

    /**
     * Custom Exception Handler.
     * @param ex an UserFavoriteStationsFetchException.
     * @return an ApiErrorResponse describing the error.
     */
    @ExceptionHandler(UserFavoriteStationsFetchException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiErrorResponse> invalidVarLength(UserFavoriteStationsFetchException ex)
    {
        return ControllerExceptionBuilder.buildErrorResponseAndPrintStackTrace(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
    }



    @ExceptionHandler(StationDoesntExistIntoTheDatabaseException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiErrorResponse> stationDoesNotExist(StationDoesntExistIntoTheDatabaseException ex)
    {
        return ControllerExceptionBuilder.buildErrorResponseAndPrintStackTrace(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
    }

    @ExceptionHandler(StationAlreadyInUserFavoriteStationsException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorResponse> stationAlreadyInUserFavoriteStations(StationAlreadyInUserFavoriteStationsException ex)
    {
        return ControllerExceptionBuilder.buildErrorResponseAndPrintStackTrace(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
    }




}
