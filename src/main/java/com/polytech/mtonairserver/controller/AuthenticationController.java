package com.polytech.mtonairserver.controller;

import com.nimbusds.oauth2.sdk.ErrorResponse;
import com.polytech.mtonairserver.config.JwtTokenUtil;
import com.polytech.mtonairserver.config.SwaggerConfig;
import com.polytech.mtonairserver.customexceptions.LoggableException;
import com.polytech.mtonairserver.customexceptions.accountcreation.*;
import com.polytech.mtonairserver.customexceptions.loginexception.*;
import com.polytech.mtonairserver.model.entities.UserEntity;
import com.polytech.mtonairserver.model.responses.*;
import com.polytech.mtonairserver.service.implementation.JwtUserDetailsService;
import com.polytech.mtonairserver.service.implementation.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import static com.polytech.mtonairserver.customexceptions.ControllerExceptionBuilder.buildErrorResponseAndPrintStackTrace;

@RestController
@CrossOrigin
@Api(tags = SwaggerConfig.AUTHENTICATION_NAME_TAG)
@RequestMapping(value = "/auth")
public class AuthenticationController {
    private final UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    public AuthenticationController(UserService _userService) {
        this.userService = _userService;
    }

    /**
     * Allows the user authentication and generate a token.
     *
     * @param loginPassword a json body that will be deserialized into a UserEntity and that contains.
     * @return an ApiAuthenticateSuccessResponse with the user id and the user api token.
     */
    @ApiOperation(value = "User authentication", notes = "The user authenticates to his M-Ton-Air account")
    @RequestMapping(value = "/sign-in", method = RequestMethod.POST)
    public ResponseEntity login(@ApiParam(name = "loginPassword", value = "The user login and password", required = true)
                                @RequestBody UserEntity loginPassword) throws Exception {

        try {
            // We control the user
            authenticate(loginPassword.getEmail(), loginPassword.getPassword());

            UserEntity ue = this.userService.login(loginPassword);

            // We retrieve the information
            // New access to the database
            final UserDetails userDetails = userDetailsService.loadUserByUsername(loginPassword.getEmail());
            // The token is generated
            final String token = jwtTokenUtil.generateToken(userDetails);

            return new ResponseEntity<ApiAuthenticateSuccessResponse>(
                    new ApiAuthenticateSuccessResponse(HttpStatus.OK, "The user " + ue.getFirstname() + " " + ue.getName() +
                            " (" + ue.getEmail() + ") is well authenticated.", ue.getIdUser(), ue.getApiKey(), token),
                    HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<ApiErrorResponse>(
                new ApiErrorResponse(HttpStatus.NOT_FOUND, "Could not find a record to set up a new jwt.",e), HttpStatus.NOT_FOUND);
        }

    }

    private void authenticate(String username, String password) throws Exception {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    /**
     * Method that creates an user account.
     *
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
    public ResponseEntity<ApiSuccessResponse> createAccount(@RequestBody UserEntity namesLoginPassword) throws LoggableException {
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
    private ResponseEntity<ApiErrorResponse> createAccountEmailExistsResponse(AccountAlreadyExistsException ex) {
        return buildErrorResponseAndPrintStackTrace(HttpStatus.CONFLICT, "Email is already registered. (" + ex.getExistingMail() + ")", ex);
    }

    @ExceptionHandler(InvalidEmailException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<ApiErrorResponse> createAccountUnvalidEmailResponse(InvalidEmailException ex) {
        return buildErrorResponseAndPrintStackTrace(HttpStatus.BAD_REQUEST, "The given e-mail was incorrect (" + ex.getInvalidMail() + ")", ex);
    }

    @ExceptionHandler(AccountSaveException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<ApiErrorResponse> createAccountUnvalidEmailResponse(AccountSaveException ex) {
        return buildErrorResponseAndPrintStackTrace(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong on server side while saving the given user to our database.", ex);
    }

    @ExceptionHandler(NamesMissingException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<ApiErrorResponse> missingNamesResponse(NamesMissingException ex) {
        return buildErrorResponseAndPrintStackTrace(HttpStatus.BAD_REQUEST, "Names are missing.", ex);
    }

    @ExceptionHandler(InvalidVariablesLengthException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<ApiErrorResponse> invalidVarLength(InvalidVariablesLengthException ex) {
        return buildErrorResponseAndPrintStackTrace(HttpStatus.BAD_REQUEST, "Invalid variables length", ex);
    }

    @ExceptionHandler(TokenGenerationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<ApiErrorResponse> tokenError(TokenGenerationException ex) {
        return buildErrorResponseAndPrintStackTrace(HttpStatus.INTERNAL_SERVER_ERROR, "A unique token could not be found.", ex);
    }

    @ExceptionHandler(UnknownEmailException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<ApiErrorResponse> unknownEmailResponse(UnknownEmailException ex) {
        return buildErrorResponseAndPrintStackTrace(HttpStatus.BAD_REQUEST, "The entered email does not exist in our database.", ex);
    }

    @ExceptionHandler(WrongPasswordException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<ApiErrorResponse> wrongPasswordResponse(WrongPasswordException ex) {
        return buildErrorResponseAndPrintStackTrace(HttpStatus.BAD_REQUEST, "The entered password is wrong.", ex);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<ApiErrorResponse> globalException(Exception ex) {
        return buildErrorResponseAndPrintStackTrace(HttpStatus.INTERNAL_SERVER_ERROR, "An unknown exception occured.", ex);
    }
}
