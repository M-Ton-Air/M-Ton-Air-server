package com.polytech.mtonairserver.controller;

import com.nimbusds.oauth2.sdk.ErrorResponse;
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

import static com.polytech.mtonairserver.customexceptions.ControllerExceptionBuilder.buildErrorResponseAndPrintStackTrace;

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
     * @return 200 OK and a custom message if the account creation was okay. 409 if account with the given email already exists.
     * UserEntity should have the following fields correctly set :
     * - Name
     * - First Name
     * - email
     * - password
     */
    @ApiOperation(value = "Create an account", notes = "Allows an user to create an account with a POST request to the API. It creates an user and stores it.")
    @RequestMapping(value = "/sign-up", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ApiSuccessResponse> createAccount(@RequestBody UserEntity namesLoginPassword) throws LoggableException
    {
        this.userService.createAccount(namesLoginPassword);

        return new ResponseEntity<ApiSuccessResponse>(
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


    @ExceptionHandler(AccountAlreadyExistsException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    private ResponseEntity<ApiErrorResponse> createAccountEmailExistsResponse(AccountAlreadyExistsException ex)
    {
        return buildErrorResponseAndPrintStackTrace(HttpStatus.CONFLICT, "Email is already registered. (" + ex.getExistingMail() + ")", ex);
    }

    @ExceptionHandler(InvalidEmailException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<ApiErrorResponse> createAccountUnvalidEmailResponse(InvalidEmailException ex)
    {
        return buildErrorResponseAndPrintStackTrace(HttpStatus.BAD_REQUEST, "The given e-mail was incorrect (" + ex.getInvalidMail() + ")", ex);
    }

    @ExceptionHandler(AccountSaveException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<ApiErrorResponse> createAccountUnvalidEmailResponse(AccountSaveException ex)
    {
        return buildErrorResponseAndPrintStackTrace(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong on server side while saving the given user to our database.", ex);
    }

    @ExceptionHandler(NamesMissingException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<ApiErrorResponse> missingNamesResponse(NamesMissingException ex)
    {
        return buildErrorResponseAndPrintStackTrace(HttpStatus.BAD_REQUEST, "Names are missing.", ex);
    }

    @ExceptionHandler(InvalidVariablesLengthException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<ApiErrorResponse> invalidVarLength(InvalidVariablesLengthException ex)
    {
        return buildErrorResponseAndPrintStackTrace(HttpStatus.BAD_REQUEST, "Invalid variables length", ex);
    }

    @ExceptionHandler(TokenGenerationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<ApiErrorResponse> tokenError(TokenGenerationException ex)
    {
        return buildErrorResponseAndPrintStackTrace(HttpStatus.INTERNAL_SERVER_ERROR, "A unique token could not be found.", ex);
    }

    @ExceptionHandler(UnknownEmailException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<ApiErrorResponse> unknownEmailResponse(UnknownEmailException ex)
    {
        return buildErrorResponseAndPrintStackTrace(HttpStatus.BAD_REQUEST, "The entered email does not exist in our database.", ex);
    }

    @ExceptionHandler(WrongPasswordException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<ApiErrorResponse> wrongPasswordResponse(WrongPasswordException ex)
    {
        return buildErrorResponseAndPrintStackTrace(HttpStatus.BAD_REQUEST, "The entered password is wrong.", ex);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<ApiErrorResponse> globalException(Exception ex)
    {
        return buildErrorResponseAndPrintStackTrace(HttpStatus.INTERNAL_SERVER_ERROR, "An unknown exception occured.", ex);
    }
}
