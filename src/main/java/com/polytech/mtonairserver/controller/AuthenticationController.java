package com.polytech.mtonairserver.controller;

import com.polytech.mtonairserver.config.SwaggerConfig;
import com.polytech.mtonairserver.customexceptions.LoggableException;
import com.polytech.mtonairserver.customexceptions.accountcreation.*;
import com.polytech.mtonairserver.customexceptions.loginexception.*;
import com.polytech.mtonairserver.model.entities.UserEntity;
import com.polytech.mtonairserver.model.responses.*;
import com.polytech.mtonairserver.service.implementation.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = SwaggerConfig.AUTHENTICATION_NAME_TAG)
@RequestMapping(value = "/auth")
public class AuthenticationController
{
    private final UserService userService;

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
    @RequestMapping(value = "/sign-in", method = RequestMethod.POST)
    public ResponseEntity login(@ApiParam(name = "loginPassword", value = "The user login and password", required = true)
                                @RequestBody UserEntity loginPassword) throws UnknownEmailException, WrongPasswordException
    {
        UserEntity ue = this.userService.login(loginPassword);
        return new ResponseEntity<ApiAuthenticateSuccessResponse>(
                new ApiAuthenticateSuccessResponse(HttpStatus.OK, "The user " + ue.getFirstname() + " " + ue.getName() +
                " (" + ue.getEmail() + ") is well authenticated.", ue.getIdUser(), ue.getApiKey()),
                HttpStatus.OK);
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
    @RequestMapping(value = "/sign-up", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity createAccount(@RequestBody UserEntity namesLoginPassword) throws LoggableException
    {
        this.userService.createAccount(namesLoginPassword);
        return new ResponseEntity<ApiSuccessResponse>
        (
            new ApiSuccessResponse(
                HttpStatus.OK,
                "Account was successfully created. Welcome "
                        + namesLoginPassword.getName()
                        + " ("
                        + namesLoginPassword.getEmail()
                        + ")"),
            HttpStatus.OK
        );
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
    private ResponseEntity createAccountEmailExistsResponse(AccountAlreadyExistsException ex)
    {
        // ignores unuseful elements
        ex.setStackTrace(new StackTraceElement[]{ex.getStackTrace()[0]});
        return new ResponseEntity<ApiErrorResponse>(
                new ApiErrorResponse(HttpStatus.CONFLICT, "The given e-mail already login (" + ex.getExistingMail() + ")", ex),
                HttpStatus.CONFLICT);
    }

    /**
     * Custom Exception Handler for invalid emails.
     * @param ex an InvalidEmailException
     * @return an api error response describing what went wrong to the api user.
     */
    @ExceptionHandler(InvalidEmailException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ApiErrorResponse createAccountUnvalidEmailResponse(InvalidEmailException ex)
    {
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
    private ApiErrorResponse createAccountUnvalidEmailResponse(AccountSaveException ex)
    {
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
    private ApiErrorResponse missingNamesResponse(NamesMissingException ex)
    {
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
    private ApiErrorResponse invalidVarLength(InvalidVariablesLengthException ex)
    {
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
    private ApiErrorResponse tokenError(TokenGenerationException ex)
    {
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
    private ApiErrorResponse unknownEmailResponse(UnknownEmailException ex)
    {
        ex.setStackTrace(new StackTraceElement[]{ex.getStackTrace()[0]});
        return new ApiErrorResponse(HttpStatus.BAD_REQUEST, "The entered email does not exist in our database.", ex);
    }

    /**
     * Custom Exception Handler for wrong passwords.
     * @param ex an WrongPasswordException.
     * @return an ApiErrorResponse describing the error.
     */
    @ExceptionHandler(WrongPasswordException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ApiErrorResponse wrongPasswordResponse(WrongPasswordException ex)
    {
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
    private ApiErrorResponse globalException(Exception ex)
    {
        ex.setStackTrace(new StackTraceElement[]{ex.getStackTrace()[0]});
        return new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unknown exception occured.", ex);
    }
}
