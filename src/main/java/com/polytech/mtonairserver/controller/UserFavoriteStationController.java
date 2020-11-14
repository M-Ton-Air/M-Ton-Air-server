package com.polytech.mtonairserver.controller;

import com.polytech.mtonairserver.config.SwaggerConfig;
import com.polytech.mtonairserver.customexceptions.favoritestation.StationAlreadyInUserFavoriteStationsException;
import com.polytech.mtonairserver.customexceptions.miscellaneous.UserFavoriteStationsFetchException;
import com.polytech.mtonairserver.model.entities.StationEntity;
import com.polytech.mtonairserver.model.responses.ApiErrorResponse;
import com.polytech.mtonairserver.model.responses.ApiResponse;
import com.polytech.mtonairserver.model.responses.ApiSuccessResponse;
import com.polytech.mtonairserver.service.implementation.UserFavoriteStationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@Api(tags = SwaggerConfig.USER_FAVORITE_STATION_NAME_TAG)
@RequestMapping("/favorite-station/user")
public class UserFavoriteStationController {

    private UserFavoriteStationService userFavoriteStationService;

    @Autowired
    public UserFavoriteStationController(UserFavoriteStationService userFavoriteStationService) {
        this.userFavoriteStationService = userFavoriteStationService;
    }

    @ApiOperation(value = "Gets the favorite stations for a given user", notes = "Returns 0, one or many stations" +
            " according to the user favorite stations.")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Set<StationEntity>> getUserFavoriteStations(
            @ApiParam(name = "id", value = "The user id", required = true)
            @PathVariable int id) throws UserFavoriteStationsFetchException
    {
        try
        {
            return new ResponseEntity<Set<StationEntity>>(this.userFavoriteStationService.listUserFavoriteStations(id), HttpStatus.OK);
        }
        catch(Exception e)
        {
            throw new UserFavoriteStationsFetchException(e.getMessage(), UserController.class);
        }
    }



    @ApiOperation(value = "Add a station into a specific user favorite stations", notes = "Add a user favorite station")
    @RequestMapping(value = "/{idUser}/station/{idStation}/add", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse> addAUserFavoriteStation(
            @ApiParam(name = "idUser", value = "The user id", required = true)
            @PathVariable int idUser,
            @ApiParam(name = "idStation", value = "The station id", required = true)
            @PathVariable int idStation) throws StationAlreadyInUserFavoriteStationsException {

        try {
            this.userFavoriteStationService.addUserFavoriteStation(idUser, idStation);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<ApiResponse>(new ApiErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Could not add the station n°" + idStation + " to user n°" + idUser + " favorite stations", e),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<ApiResponse>(
                new ApiSuccessResponse(
                        HttpStatus.OK,
                        "The station n°" + idStation + " is added to user n°" + idUser + " favorite stations"),
                HttpStatus.OK
        );
    }

    @ApiOperation(value = "Delete all user favorite stations", notes = "Delete all favorite stations to a specific user")
    @RequestMapping(value = "/{idUser}/delete", method = RequestMethod.DELETE)
    @Transactional
    public ResponseEntity<ApiResponse> deleteAllUserFavoriteStations(
            @ApiParam(name = "idUser", value = "The user id", required = true)
            @PathVariable int idUser) {

        try {
            this.userFavoriteStationService.deleteAllUserFavoriteStations(idUser);
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
    @RequestMapping(value = "/{idUser}/station/{idStation}/delete", method = RequestMethod.DELETE)
    @Transactional
    public ResponseEntity<ApiResponse> deleteSpecificUserFavoriteStation(
            @ApiParam(name = "idUser", value = "The user id", required = true)
            @PathVariable int idUser,
            @ApiParam(name = "idStation", value = "The station id", required = true)
            @PathVariable int idStation) {

        try {
            this.userFavoriteStationService.deleteSpecificUserFavoriteStation(idUser, idStation);
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
    public ApiErrorResponse invalidVarLength(UserFavoriteStationsFetchException ex)
    {
        ex.printStackTrace();
        ex.setStackTrace(new StackTraceElement[]{ex.getStackTrace()[0]});
        return new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Could not fetch the user favorite stations. Server side error occured.", ex);
    }
}
