package com.polytech.mtonairserver.dao;

import com.polytech.mtonairserver.domains.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    public static List<UserEntity> users = new ArrayList<>();

    static {
        UserEntity ue = new UserEntity();
        ue.setIdUser(3);
        ue.setName("lolo");
        ue.setFirstname("lala");
        ue.setEmail("lala.lolo@etudiant.fr");
        ue.setPassword("91011a");
        ue.setApiKey("okokokokookook");

        users.add(ue);

    }


    @Override
    public List<UserEntity> findAll() {
        return users;
    }

    @Override
    public UserEntity findById(int id) {
        for(UserEntity user : users) {
            if(user.getIdUser() == id) {
                return user;
            }
        }
        return null;
    }

    @Override
    public UserEntity save(UserEntity user) {
        users.add(user);
        return user;
    }
}
