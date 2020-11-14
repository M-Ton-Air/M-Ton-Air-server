package com.polytech.mtonairserver.repository;

import com.polytech.mtonairserver.model.entities.StationEntity;
import com.polytech.mtonairserver.model.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Public repository operations for the users entities.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    public UserEntity findAllByIdUser(int idUser);

    public UserEntity findByEmail(String email);

    public boolean existsByEmail(String email);

    public boolean existsByApiKey(String email);

    //public void

}
