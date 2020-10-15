package com.polytech.mtonairserver;

import com.polytech.mtonairserver.controller.AuthenticationController;
import com.polytech.mtonairserver.controller.UserController;
import com.polytech.mtonairserver.customexceptions.loginexception.UnknownEmailException;
import com.polytech.mtonairserver.customexceptions.loginexception.WrongPasswordException;
import com.polytech.mtonairserver.model.entities.UserEntity;
import com.polytech.mtonairserver.model.responses.ApiAuthenticateSuccessResponse;
import com.polytech.mtonairserver.model.responses.ApiSuccessResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

// todo : classe à implémenter

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class AuthenticationControllerTests {

    @MockBean
    private AuthenticationController authenticationController;

    @Test
    public void authentication() throws UnknownEmailException, WrongPasswordException {

      //  UserEntity userEntity = new UserEntity(1, "", "", "dorian.gg@gmail.com", "12345678", "");
     //   ApiAuthenticateSuccessResponse apiAuthenticateSuccessResponse = new ApiAuthenticateSuccessResponse(HttpStatus.OK, "okokok", 1, "kjldjdpjdpojdj");
       // Mockito.when(authenticationController.login(userEntity)).thenReturn(apiAuthenticateSuccessResponse);

    }
}
