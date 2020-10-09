package com.polytech.mtonairserver.dto;

import com.polytech.mtonairserver.domains.UserEntity;

public class LoginResponseDto {

    private UserEntity user;
    private String token;

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
