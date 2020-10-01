package com.polytech.mtonairserver.dao;

import com.polytech.mtonairserver.domains.UserEntity;

import java.util.List;

public interface UserDao {
    public List<UserEntity>findAll();
    public UserEntity findById(int id);
    public UserEntity save(UserEntity user);
}
