package com.polytech.mtonairserver.controller;

import com.polytech.mtonairserver.config.SwaggerConfig;
import com.polytech.mtonairserver.model.entities.UserEntity;
import com.polytech.mtonairserver.service.implementation.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

}
