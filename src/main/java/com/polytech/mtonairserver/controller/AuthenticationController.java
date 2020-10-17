package com.polytech.mtonairserver.controller;

import com.polytech.mtonairserver.config.SwaggerConfig;
import com.polytech.mtonairserver.customexceptions.LoggableException;
import com.polytech.mtonairserver.customexceptions.accountcreation.*;
import com.polytech.mtonairserver.customexceptions.loginexception.UnknownEmailException;
import com.polytech.mtonairserver.customexceptions.loginexception.WrongPasswordException;
import com.polytech.mtonairserver.model.entities.UserEntity;
import com.polytech.mtonairserver.model.responses.ApiAuthenticateSuccessResponse;
import com.polytech.mtonairserver.model.responses.ApiErrorResponse;
import com.polytech.mtonairserver.model.responses.ApiResponse;
import com.polytech.mtonairserver.model.responses.ApiSuccessResponse;
import com.polytech.mtonairserver.service.implementation.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = SwaggerConfig.AUTHENTICATION_NAME_TAG)
@RequestMapping(value = "/auth")
public class AuthenticationController
{
    private UserService userService;

    @Autowired
    public AuthenticationController(UserService _userService) {
        this.userService = _userService;
    }


    /**
     * Allows the user authentication.
     * @param loginPassword a json body that will be deserialized into a UserEntity and that contains.
     * @return an ApiAuthenticateSuccessResponse with the user id and the user api token.
     */
    @ApiOperation(value = "User authentication", notes = "The user authenticates to his M-Ton-Air account")
    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public ApiAuthenticateSuccessResponse login(@ApiParam(name = "loginPassword", value = "The user login and password", required = true)
                                @RequestBody UserEntity loginPassword) throws UnknownEmailException, WrongPasswordException
    {


        UserEntity ue = this.userService.login(loginPassword);
        return new ApiAuthenticateSuccessResponse(HttpStatus.OK, "The user " + ue.getFirstname() + " " + ue.getName() +
                " (" + ue.getEmail() + ") is well authenticated.", ue.getIdUser(), ue.getApiKey());

            // todo returns an ApiAuthenticateSuccessResponse with the user id + user api token. Client then has to store it locally.
    }

    /**
     * Method that creates an user account.
     * @param namesLoginPassword the user login and password.
     * @return 200 OK and a custom message if the account creation was okay. 409 if account with the given email already login.
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
        try
        {
            this.userService.createAccount(namesLoginPassword);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Cold not createAccount the specified UserEntity : \n " + namesLoginPassword.getEmail(), e);
        }

        return new ApiSuccessResponse(HttpStatus.OK,
                "Account was successfully created. Welcome "
                        + namesLoginPassword.getFirstname()
                        + " ("
                        + namesLoginPassword.getEmail()
                        + ")");
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
        return new ApiErrorResponse(HttpStatus.CONFLICT, "The given e-mail already login (" + ex.getExistingMail() + ")", ex);
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
        return new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong on server side while saving the given user to our database.", ex);
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

    /**
     * Custom Exception Handler for wrong passwords.
     * @param ex an WrongPasswordException.
     * @return an ApiErrorResponse describing the error.
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse globalException(Exception ex) {
            ex.setStackTrace(new StackTraceElement[]{ex.getStackTrace()[0]});
        return new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unknown exception occured.", ex);
    }
}
