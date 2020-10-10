package com.polytech.mtonairserver.controller;

import com.polytech.mtonairserver.config.SwaggerConfig;
import com.polytech.mtonairserver.customexceptions.accountcreation.*;
import com.polytech.mtonairserver.model.responses.ApiErrorResponse;
import com.polytech.mtonairserver.model.entities.UserEntity;
import com.polytech.mtonairserver.model.responses.ApiResponse;
import com.polytech.mtonairserver.model.responses.ApiSuccessResponse;
import com.polytech.mtonairserver.repositories.UserEntityRepository;
import com.polytech.mtonairserver.security.TokenGenerator;
import io.swagger.annotations.Api;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public UserEntity login(@RequestBody UserEntity loginPassword) {
        //System.out.println(loginPassword);
        return null;
    }

    /**
     * Method that creates an user account.
     * @param namesLoginPassword the user login and password.
     * @return 200 OK and a custom message if the account creation was okay. 409 if account with the given email already exists.
     * UserEntity should have the following fields correctly set :
     * - Name
     * - First Name
     * - email
     * - password
     */
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse createAccount(@RequestBody UserEntity namesLoginPassword) throws AccountCreationException
    {
        // when no names are given
        if(namesLoginPassword.getName().isEmpty() || namesLoginPassword.getFirstname().isEmpty())
        {
            throw new NamesMissingException("Name and First name were not specified", AuthenticationController.class);
        }
        
        // todo : handle the following cases :
        /*
        pw : between 6 and 32 chars
        names between 1 to 50 chars
        email : up to 75chars
         */

        String userEmail = namesLoginPassword.getEmail();
        // Is email correctly formed ?
        EmailValidator validator = EmailValidator.getInstance();
        if(!validator.isValid(userEmail))
        {
            throw new UnvalidEmailException("Email " + userEmail + " is invalid.", AuthenticationController.class, userEmail);
        }



        // If it is valid, then we can check if the user already has an account or not.
        // If he already has one, server returns a 409 conflict error code with a message.
        // Checking if email is already used
        if(this.userEntityRepository.existsByEmail(userEmail))
        {
            throw new AccountAlreadyExistsException("Email already exists.", AuthenticationController.class, namesLoginPassword.getEmail());
        }

        // if email is valid + account does not already exists, we can begin the account creation process.

        // generating a unique api token for our user
        namesLoginPassword.setApiKey(TokenGenerator.generateUserApiToken());
        // hashing the user pw
        namesLoginPassword.setPassword(this.pwHasher.encode(namesLoginPassword.getPassword()));
        try
        {
            this.userEntityRepository.save(namesLoginPassword);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Cold not save the specified UserEntity : \n " + userEmail.toString(), e);
        }
        return new ApiSuccessResponse(HttpStatus.OK,
                "Account was successfully created. Welcome "
                        + namesLoginPassword.getFirstname()
                        + " "
                        + namesLoginPassword.getName()
                        + " ("
                        + namesLoginPassword.getEmail()
                        + ")");
    }

    /* ############################################################## EXCEPTION HANDLERS ############################################################## */


    /**
     * Custom Exception Handler that will provide data to the API user when an AccountAlreadyExistsException is
     * raised.
     * @param ex the raised exception.
     * @return an ApiErrorResponse describing the error
     * @see ApiErrorResponse
     */
    @ExceptionHandler(AccountAlreadyExistsException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorResponse createAccountEmailExistsResponse(AccountAlreadyExistsException ex)
    {
        // ignores unuseful elements
        ex.setStackTrace(new StackTraceElement[]{ex.getStackTrace()[0]});
        return new ApiErrorResponse(HttpStatus.CONFLICT, "The given e-mail already exists (" + ex.getExistingMail() + ")", ex);
    }

    /**
     * Custom Exception Handler for invalid emails.
     * @param ex an UnvalidEmailException
     * @return an api error response describing what went wrong to the api user.
     */
    @ExceptionHandler(UnvalidEmailException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse createAccountUnvalidEmailResponse(UnvalidEmailException ex)
    {
        // ignores unuseful elements
        ex.setStackTrace(new StackTraceElement[]{ex.getStackTrace()[0]});
        return new ApiErrorResponse(HttpStatus.BAD_REQUEST, "The given e-mail was incorrect (" + ex.getInvalidMail() + ")", ex);
    }

    /**
     * Custom Exception Handler for account creation errors while saving an user to the db
     * @param ex an AccountSaveException
     * @return an api error response describing what went wrong to the api user.
     */
    @ExceptionHandler(AccountSaveException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorResponse createAccountUnvalidEmailResponse(AccountSaveException ex)
    {
        // ignores unuseful elements
        ex.setStackTrace(new StackTraceElement[]{ex.getStackTrace()[0]});
        return new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong on server side while saving the given user to our database. ", ex);
    }

    @ExceptionHandler(NamesMissingException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse missingNmaesResponse(NamesMissingException ex)
    {
        // ignores unuseful elements
        ex.setStackTrace(new StackTraceElement[]{ex.getStackTrace()[0]});
        return new ApiErrorResponse(HttpStatus.BAD_REQUEST, "Names are missing.", ex);
    }

}
