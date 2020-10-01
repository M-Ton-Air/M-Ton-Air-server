package com.polytech.mtonairserver.controller;

import com.polytech.mtonairserver.dao.UserDao;
import com.polytech.mtonairserver.domains.UserEntity;
import com.polytech.mtonairserver.repositories.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private UserEntityRepository userEntityRepository;

    @Autowired
    public UserController(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }

    //private UserDao userDao;

    /**
     * Retrieve the list of users
     * @return list of users
     */
    //@Operation(summary = "Get the list of users")
    @RequestMapping(value = "/users", method= RequestMethod.GET)
    public List<UserEntity> listOfUsers() {
        return userEntityRepository.findAll();
    }

    /**
     * Retrieve a user by his id
     * @param id user's id
     * @return a specific user
     */
    //@Operation(summary = "Get a user by his id")
    @RequestMapping(value = "/users/{id}", method= RequestMethod.GET)
    public UserEntity displayAUser(//@Parameter(description = "id of user to be searched")
                                       @PathVariable int id) {
       // return userDao.findById(id);
        return userEntityRepository.findByIdUser(id);
    }
}
