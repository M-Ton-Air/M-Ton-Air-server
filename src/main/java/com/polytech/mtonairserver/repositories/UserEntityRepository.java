package com.polytech.mtonairserver.repositories;

import com.polytech.mtonairserver.domains.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Integer> {
    List<UserEntity> findAll();
    UserEntity findByIdUser(int id);
    //Optional<UserEntity> findByEmail(String email);
    Boolean existsByEmail(String email);
    UserEntity findByEmail(String email);
}
