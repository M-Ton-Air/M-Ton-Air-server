package com.polytech.mtonairserver.repository;

import com.polytech.mtonairserver.model.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Public operations for the users entities.
 */
@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Integer> {


}
