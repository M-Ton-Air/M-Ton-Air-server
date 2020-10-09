package com.polytech.mtonairserver.controller;

import com.polytech.mtonairserver.config.SwaggerConfig;
import com.polytech.mtonairserver.model.UserEntity;
import com.polytech.mtonairserver.repositories.UserEntityRepository;
import io.swagger.annotations.*;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = SwaggerConfig.USERS_NAME_TAG)
@RequestMapping("/users")
public class UserController {

    private UserEntityRepository userEntityRepository;

    @Autowired
    public UserController(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }

    //private UserDao userDao;

    /**
     * Retrieve the users list
     * @return users list
     */
    @ApiOperation(value = "Get the users list", notes = "gets all the" +
            "available users stored in the M-Ton-Air database.")
    @RequestMapping(method= RequestMethod.GET)
    public List<UserEntity> listOfUsers() {
        return userEntityRepository.findAll();
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
        return userEntityRepository.findByIdUser(id);
    }

}
