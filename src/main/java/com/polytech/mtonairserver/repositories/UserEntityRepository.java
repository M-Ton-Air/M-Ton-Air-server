package com.polytech.mtonairserver.repositories;

import com.polytech.mtonairserver.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserEntityRepository extends JpaRepository<UserEntity, Integer> {
    List<UserEntity> findAll();
    UserEntity findByIdUser(int id);
}
