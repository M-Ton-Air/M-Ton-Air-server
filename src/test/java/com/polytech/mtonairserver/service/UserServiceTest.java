package com.polytech.mtonairserver.service;

import com.polytech.mtonairserver.customexceptions.accountcreation.*;
import com.polytech.mtonairserver.customexceptions.loginexception.UnknownEmailException;
import com.polytech.mtonairserver.customexceptions.loginexception.WrongPasswordException;
import com.polytech.mtonairserver.model.entities.UserEntity;
import com.polytech.mtonairserver.repository.UserRepository;
import com.polytech.mtonairserver.service.implementation.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;
import java.security.SecureRandom;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

/**
 * Testing the {@link UserService} class methods (login and account creation).
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest
{
    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder pwHasher;

    @InjectMocks
    private UserService userService;

    @Test
    void accountCreationshouldThrowNamesMissingException()
    {
        final UserEntity userWithoutNames = new UserEntity(1, "", "", "", "", "");

        Assert.assertThrows(NamesMissingException.class, () ->
        {
           this.userService.createAccount(userWithoutNames);
        });

    }

    @Test
    void accountCreationshouldThrowInvalidVariablesLengthException()
    {
        final UserEntity userWithNamesThatAreTooLong = new UserEntity(1, "Do", "NaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNaNa", "anemail.com", "123456789", "");

        Assert.assertThrows(InvalidVariablesLengthException.class, () ->
        {
            this.userService.createAccount(userWithNamesThatAreTooLong);
        });
    }

    @Test
    void accountCreationshouldThrowInvalidEmailException()
    {
        final UserEntity userWithBadEmail = new UserEntity(1, "Do", "Na", "anemail.com", "123456789", "");

        Assert.assertThrows(InvalidEmailException.class, () ->
        {
            this.userService.createAccount(userWithBadEmail);
        });
    }

    @Test
    void accountCreationshouldThrowAccountAlreadyExistsException()
    {
        given(this.userService.existsByEmail("dorian1@gmail.com")).willReturn(true);

        final UserEntity userWithAlreadyTakenEmail = new UserEntity(1, "Do", "Na", "dorian1@gmail.com", "123456789", "");

        Assert.assertThrows(AccountAlreadyExistsException.class, () ->
        {
           this.userService.createAccount(userWithAlreadyTakenEmail);
        });
    }

    @Test
    void accountCreationshouldThrowTokenGenerationException()
    {
        given(this.userService.existsByApiKey(any(String.class))).willReturn(true);

        final UserEntity userWithGoodParams = new UserEntity(1, "Do", "Na", "dorian1@gmail.com", "123456789", "");

        Assert.assertThrows(TokenGenerationException.class, () ->
        {
            this.userService.createAccount(userWithGoodParams);
        });
    }

    @Test
    void accountCreationshouldWork() throws InvalidVariablesLengthException, InvalidEmailException, NamesMissingException, TokenGenerationException, AccountAlreadyExistsException
    {
        final UserEntity userWithGoodParams = new UserEntity(1, "Do", "Na", "dorian1@gmail.com", "123456789", "");
        Assertions.assertAll(() ->
        {
            this.userService.createAccount(userWithGoodParams);
        });
    }

    @Test
    void loginShouldThrowUnknownEmailException()
    {
        final UserEntity userWithUnknownEmail = new UserEntity(1, "Do", "Na", "dorian1@gmail.com", "123456789", "");
        given(this.userService.existsByEmail("dorian1@gmail.com")).willReturn(false);

        Assert.assertThrows(UnknownEmailException.class, () ->
        {
            this.userService.login(userWithUnknownEmail);
        });
    }

    @Test
    //$2a$10$qDVOXgJcAY.SpT2gPkbytOmQBGuM2UcdGtrSACiWIqtj0q7ns7WNW"
    void loginShouldThrowWrongPasswordException() throws NoSuchFieldException, IllegalAccessException, UnknownEmailException, WrongPasswordException
    {
        final UserEntity userWithGoodParams = new UserEntity(1, "Do", "Na", "dorian1@gmail.com", "123456789", "");
        final UserEntity userFromDatabase = new UserEntity(1, "Do", "Na", "dorian1@gmail.com", "i'm encoded", "");

        // replacing the pw hasher field by the mocked one.
        Field pwHasherField = this.userService.getClass().getDeclaredField("pwHasher");
        pwHasherField.setAccessible(true);
        pwHasherField.set(this.userService, this.pwHasher);

        given(this.userService.existsByEmail("dorian1@gmail.com")).willReturn(true);
        given(this.userService.findByEmail("dorian1@gmail.com")).willReturn(userFromDatabase);
        given(this.pwHasher.matches(userWithGoodParams.getPassword(), "i'm encoded")).willReturn(false);

        Assert.assertThrows(WrongPasswordException.class, () ->
        {
            this.userService.login(userWithGoodParams);
        });
    }

    void loginShouldWork() throws NoSuchFieldException, IllegalAccessException, UnknownEmailException, WrongPasswordException
    {
        final UserEntity userWithGoodParams = new UserEntity(1, "Do", "Na", "dorian1@gmail.com", "123456789", "");
        final UserEntity userFromDatabase = new UserEntity(1, "Do", "Na", "dorian1@gmail.com", "i'm encoded", "");

        // replacing the pw hasher field by the mocked one.
        Field pwHasherField = this.userService.getClass().getDeclaredField("pwHasher");
        pwHasherField.setAccessible(true);
        pwHasherField.set(this.userService, this.pwHasher);

        given(this.userService.existsByEmail("dorian1@gmail.com")).willReturn(true);
        given(this.userService.findByEmail("dorian1@gmail.com")).willReturn(userFromDatabase);
        given(this.pwHasher.matches(userWithGoodParams.getPassword(), "i'm encoded")).willReturn(true);

        Assert.assertThrows(WrongPasswordException.class, () ->
        {
            this.userService.login(userWithGoodParams);
        });
    }
}
