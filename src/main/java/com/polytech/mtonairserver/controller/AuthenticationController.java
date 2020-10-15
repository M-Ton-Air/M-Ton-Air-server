package com.polytech.mtonairserver.controller;

import com.polytech.mtonairserver.config.SwaggerConfig;
import com.polytech.mtonairserver.customexceptions.LoggableException;
import com.polytech.mtonairserver.customexceptions.accountcreation.*;
import com.polytech.mtonairserver.customexceptions.loginexception.UnknownEmailException;
import com.polytech.mtonairserver.customexceptions.loginexception.WrongPasswordException;
import com.polytech.mtonairserver.model.responses.ApiAuthenticateSuccessResponse;
import com.polytech.mtonairserver.model.responses.ApiErrorResponse;
import com.polytech.mtonairserver.model.entities.UserEntity;
import com.polytech.mtonairserver.model.responses.ApiResponse;
import com.polytech.mtonairserver.model.responses.ApiSuccessResponse;
import com.polytech.mtonairserver.repositories.UserEntityRepository;
import com.polytech.mtonairserver.security.TokenGenerator;
import io.swagger.annotations.Api;
import org.apache.catalina.User;
import org.apache.commons.validator.routines.EmailValidator;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.HashMap;

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
     * Allows the user authentication.
     * @param loginPassword a json body that will be deserialized into a UserEntity and that contains.
     * @return an ApiAuthenticateSuccessResponse with the user id and the user api token.
     */
    @ApiOperation(value = "User authentication", notes = "The user authenticates to his M-Ton-Air account")
    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public ApiAuthenticateSuccessResponse login(@ApiParam(name = "loginPassword", value = "The user login and password", required = true)
                                @RequestBody UserEntity loginPassword) throws UnknownEmailException, WrongPasswordException {

        //check if the user exists
        if (userEntityRepository.existsByEmail(loginPassword.getEmail())) {
            // user contains all the information about the useUserEntity user = userEntityRepository.findByEmail(loginPassword.getEmail());
            UserEntity user = userEntityRepository.findByEmail(loginPassword.getEmail());

           // if the entered password does not match with the user's password
           if(!this.pwHasher.matches(loginPassword.getPassword(), user.getPassword()))
           {
               throw new WrongPasswordException("The entered password is wrong", AuthenticationController.class);
           }
           // if the entered password match with the user's password
           else {
               return new ApiAuthenticateSuccessResponse(HttpStatus.OK, "The user " + user.getFirstname() + " " + user.getName() +
                       " (" + user.getEmail() + ") is well authenticated.", user.getIdUser(), user.getApiKey());
           }
        }
        // if the user does not exists
        else {
            throw new UnknownEmailException("Email " + loginPassword + " is unknown.", AuthenticationController.class, loginPassword.getEmail());
        }
            // todo returns an ApiAuthenticateSuccessResponse with the user id + user api token. Client then has to store it locally.
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
    @ApiOperation(value = "Create an account", notes = "Allows an user to create an account with a POST request to the API. It creates an user and stores it.")
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse createAccount(@RequestBody UserEntity namesLoginPassword) throws LoggableException
    {
        // when no names are given
        if(namesLoginPassword.getName().isEmpty() || namesLoginPassword.getFirstname().isEmpty())
        {
            throw new NamesMissingException("Name and First name were not specified", AuthenticationController.class);
        }

        /*
        pw (non hashed): between 6 and 32 chars
        names between 1 to 50 chars
        email : up to 75chars
         */
        boolean oneParamInvalid = false;
        HashMap<String, Integer> fieldsLength = new HashMap<>();
        if(! this.checkVariableLength(6, 32, namesLoginPassword.getPassword()))
        {
            oneParamInvalid = true;
            fieldsLength.put("Password", namesLoginPassword.getPassword().length());
        }
        if(! this.checkVariableLength(1, 50, namesLoginPassword.getName())
        || ! this.checkVariableLength(1, 50, namesLoginPassword.getFirstname()))
        {
            oneParamInvalid = true;
            fieldsLength.put("Name", namesLoginPassword.getName().length());
            fieldsLength.put("First name", namesLoginPassword.getFirstname().length());

        }
        if(! this.checkVariableLength(0, 75, namesLoginPassword.getEmail()))
        {
            oneParamInvalid = true;
            fieldsLength.put("E-mail", namesLoginPassword.getEmail().length());
        }

        if(oneParamInvalid)
        {
            throw new InvalidVariablesLengthException("One or many params do not have the required length.", AuthenticationController.class, fieldsLength);
        }

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
        int cpt = 0;
        while(this.userEntityRepository.existsByApiKey(namesLoginPassword.getApiKey()))
        {
            namesLoginPassword.setApiKey(TokenGenerator.generateUserApiToken());
            cpt++;
            if(cpt > 100)
            {
                throw new TokenGenerationException("Could not find a unique token on server side... Token size has to be increased.", AuthenticationController.class);
            }
        }
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
                        + " ("
                        + namesLoginPassword.getEmail()
                        + ")");
    }


    /**
     * Private helper that helps to know whether or not a given string has min to max characters.
     * @param min the minimal length of the string.
     * @param max the maximal length of the string
     * @param str the String to be analyzed
     * @return true if str has min to max (included) chars. False otherwise.
     */
    private boolean checkVariableLength(int min, int max, String str)
    {
        return (str.length() >= min && str.length() <= max);
    }


    /* ############################################################## EXCEPTION HANDLERS ############################################################## */


    /**
     * Custom Exception Handler that will provide data to the API user when an AccountAlreadyExistsException is
     * raised.
     * @param ex the raised exception.
     * @return an ApiErrorResponse describing the error.
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
     * Custom Exception Handler for account creation errors while saving an user to the db.
     * @param ex an AccountSaveException.
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

    /**
     * Custom Exception Handler for missing names.
     * @param ex an NamesMissingException.
     * @return an ApiErrorResponse describing the error.
     */
    @ExceptionHandler(NamesMissingException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse missingNamesResponse(NamesMissingException ex)
    {
        // ignores unuseful elements
        ex.setStackTrace(new StackTraceElement[]{ex.getStackTrace()[0]});
        return new ApiErrorResponse(HttpStatus.BAD_REQUEST, "Names are missing.", ex);
    }

    /**
     * Custom Exception Handler for invalid variables length.
     * @param ex an InvalidVariablesLengthException.
     * @return an ApiErrorResponse describing the error.
     */
    @ExceptionHandler(InvalidVariablesLengthException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse invalidVarLength(InvalidVariablesLengthException ex)
    {
        // ignores unuseful elements
        ex.setStackTrace(new StackTraceElement[]{ex.getStackTrace()[0]});
        return new ApiErrorResponse(HttpStatus.BAD_REQUEST, "Invalid variables length", ex);
    }

    /**
     * Custom Exception Handler for token generation.
     * @param ex an TokenGenerationException.
     * @return an ApiErrorResponse describing the error.
     */
    @ExceptionHandler(TokenGenerationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorResponse tokenError(TokenGenerationException ex)
    {
        // ignores unuseful elements
        ex.setStackTrace(new StackTraceElement[]{ex.getStackTrace()[0]});
        return new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "A unique token could not be found.", ex);
    }

    /**
     * Custom Exception Handler for non existing emails.
     * @param ex an UnknownEmailException.
     * @return an ApiErrorResponse describing the error.
     */
    @ExceptionHandler(UnknownEmailException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse unknownEmailResponse(UnknownEmailException ex) {
        // ignores unuseful elements
        ex.setStackTrace(new StackTraceElement[]{ex.getStackTrace()[0]});
        return
                new ApiErrorResponse(HttpStatus.BAD_REQUEST, "The entered email does not exist in our database.", ex);
    }

    /**
     * Custom Exception Handler for wrong passwords.
     * @param ex an WrongPasswordException.
     * @return an ApiErrorResponse describing the error.
     */
    @ExceptionHandler(WrongPasswordException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse wrongPasswordResponse(WrongPasswordException ex) {
        // ignores unuseful elements
        ex.setStackTrace(new StackTraceElement[]{ex.getStackTrace()[0]});
        return new ApiErrorResponse(HttpStatus.BAD_REQUEST, "The entered password is wrong.", ex);
    }
}