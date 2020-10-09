package com.polytech.mtonairserver.controller;

import com.google.common.hash.Hashing;
import com.polytech.mtonairserver.config.SwaggerConfig;
import com.polytech.mtonairserver.model.UserEntity;
import com.polytech.mtonairserver.repositories.UserEntityRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

@RestController
@Api(tags = SwaggerConfig.AUTHENTICATION_NAME_TAG)
@RequestMapping(value = "/auth")
public class AuthenticationController
{
    private final int encryptionStrength = 10;

    // we chose to use BCrypt for our password encryption.
    private final BCryptPasswordEncoder pwHasher = new BCryptPasswordEncoder(encryptionStrength, new SecureRandom());


    private UserEntityRepository userEntityRepository;

    @Autowired
    public AuthenticationController(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }


    /**
     * Allows the user authentication
     * @param loginPassword a json body that will be deserialized into a UserEntity and that contains
     * @return
     */
    @ApiOperation(value = "User authentication", notes = "")
    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public UserEntity login(@ApiParam(name = "loginPassword", value = "The user login and password", required = true)
                                @RequestBody UserEntity loginPassword) {
        //System.out.println(loginPassword);
        return null;
    }

    /**
     * Method that creates an user account.
     * @param loginPassword the user login and password.
     * @return 200 OK and a custom message if the account creation was okay. An error code otherwise.
     */
    @ApiOperation(value = "Create an account", notes = "The user authenticates to his M-Ton-Air account")
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public UserEntity createAccount(@ApiParam(name = "loginPassword", value = "The user login and password", required = true)
                                        @RequestBody UserEntity loginPassword) {
        String pwOriginal = loginPassword.getPassword();
        String pwHash = this.pwHasher.encode(pwOriginal);
        System.out.println("Matches ? : " + this.pwHasher.matches(pwOriginal, pwHash));

        String path = AuthenticationController.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
        logger.warn("ATTENTION CREATION DE COMPTE incomplète.");
        System.out.println(path);
        return null;
    }

}
