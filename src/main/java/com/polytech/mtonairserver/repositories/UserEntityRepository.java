package com.polytech.mtonairserver.repositories;

import com.polytech.mtonairserver.model.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Public operations for the users entities.
 */
@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Integer> {

    /**
     * Gets all the users.
     * @return all the users.
     */
    List<UserEntity> findAll();

    /**
     * Gets a user by id.
     * @param id a user id.
     * @return a user according to a given id.
     */
    UserEntity findByIdUser(int id);

    /**
     * Checks if an user with a given email exists.
     * @param email the email.
     * @return true if the user exists. false otherwise.
     */
    Boolean existsByEmail(String email);

    /**
     * Finds an user by email. Please use the existsByEmail method if you don't know if the user exists.
     * @see UserEntityRepository existsByEmail(String email) method is useful in this case.
     * @param email the user email.
     * @return the user if it does exist. null otherwise.
     */
    UserEntity findByEmail(String email);


}
