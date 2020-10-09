package com.polytech.mtonairserver.controller;

import com.google.common.hash.Hashing;
import com.polytech.mtonairserver.model.AuthForm;
import com.polytech.mtonairserver.model.UserEntity;
import com.polytech.mtonairserver.dto.LoginResponseDto;
import com.polytech.mtonairserver.repositories.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.charset.StandardCharsets;

public class AuthentificationController {

    private UserEntityRepository userEntityRepository;

    @Autowired
    public AuthentificationController(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }

    @RequestMapping(value = "/email", method = RequestMethod.POST)
    public LoginResponseDto loginUser(AuthForm authForm) {

        UserEntity userEntity = userEntityRepository.findByEmail(authForm.getEmail());
        String hashedPassword = Hashing.sha256().hashString(authForm.getPassword(), StandardCharsets.UTF_8).toString();

        if (!hashedPassword.equals(userEntity.getPassword())) {
            // throw new ... une exception
        }


            LoginResponseDto loginResponseDto = new LoginResponseDto();
            loginResponseDto.setUser(userEntity);
            loginResponseDto.setToken("blabla");
            return loginResponseDto;

    }

}
