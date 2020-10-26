package com.polytech.mtonairserver;

import com.polytech.mtonairserver.controller.AuthenticationController;
import com.polytech.mtonairserver.controller.UserController;
import com.polytech.mtonairserver.customexceptions.LoggableException;
import com.polytech.mtonairserver.customexceptions.accountcreation.NamesMissingException;
import com.polytech.mtonairserver.customexceptions.loginexception.UnknownEmailException;
import com.polytech.mtonairserver.customexceptions.loginexception.WrongPasswordException;
import com.polytech.mtonairserver.model.entities.UserEntity;
import com.polytech.mtonairserver.model.responses.ApiAuthenticateSuccessResponse;
import com.polytech.mtonairserver.model.responses.ApiSuccessResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

// todo : classe à implémenter

@SpringBootTest
public class AuthenticationControllerTests {

    @Autowired
//    private AuthenticationController authenticationController;


    @Test
    public void authenticationTest() throws LoggableException {

      //  UserEntity userEntity = new UserEntity(1, "", "", "dorian.gg@gmail.com", "12345678", "");
     //   ApiAuthenticateSuccessResponse apiAuthenticateSuccessResponse = new ApiAuthenticateSuccessResponse(HttpStatus.OK, "okokok", 1, "kjldjdpjdpojdj");
       // Mockito.when(authenticationController.login(userEntity)).thenReturn(apiAuthenticateSuccessResponse);


        UserEntity ue = new UserEntity(1, "", "", "", "" , null);
        Assert.assertThrows(NamesMissingException.class, () ->
        {
//            this.authenticationController.createAccount(ue);
        });
    }
}
