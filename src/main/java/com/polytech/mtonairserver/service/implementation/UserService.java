package com.polytech.mtonairserver.service.implementation;

import com.polytech.mtonairserver.customexceptions.accountcreation.*;
import com.polytech.mtonairserver.customexceptions.loginexception.UnknownEmailException;
import com.polytech.mtonairserver.customexceptions.loginexception.WrongPasswordException;
import com.polytech.mtonairserver.model.entities.StationEntity;
import com.polytech.mtonairserver.model.entities.UserEntity;
import com.polytech.mtonairserver.repository.UserRepository;
import com.polytech.mtonairserver.security.TokenGenerator;
import com.polytech.mtonairserver.service.interfaces.IUserService;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Service implementation for the user service.
 */
@Service
public class UserService implements IUserService
{
    private UserRepository userRepository;

    private final int encryptionStrength = 10;

    // we chose to use BCrypt for our password encryption.
    private final BCryptPasswordEncoder pwHasher = new BCryptPasswordEncoder(encryptionStrength, new SecureRandom());

    @Autowired
    public UserService(UserRepository ur)
    {
        this.userRepository = ur;
    }

    /**
     * Gets all the users.
     * @return all the users.
     */
    @Override
    public List<UserEntity> findAll()
    {
        return this.userRepository.findAll();
    }

    /**
     * Gets a user by id.
     * @param id a user id.
     * @return a user according to a given id.
     */
    @Override
    public UserEntity findByIdUser(int id)
    {
        return this.userRepository.findById(id).get();
    }

    /**
     * Checks if an user with a given email login.
     * @param email the email.
     * @return true if the user login. false otherwise.
     */
    @Override
    public boolean existsByEmail(String email)
    {
        return this.userRepository.existsByEmail(email);
    }

    /**
     * Finds an user by email. Please use the existsByEmail method if you don't know if the user login.
     * @see UserRepository existsByEmail(String email) method is useful in this case.
     * @param email the user email.
     * @return the user if it does exist. null otherwise.
     */
    @Override
    public UserEntity findByEmail(String email)
    {
        return userRepository.findByEmail(email);
    }

    /**
     * Checks if an user with the given api key already login
     * @param apiKey an api key that may login in db
     * @return true if the user with the given api key login. False otherwise.
     */
    @Override
    public boolean existsByApiKey(String apiKey)
    {
        return this.userRepository.existsByApiKey(apiKey);
    }

    @Override
    public Collection<StationEntity> listUserFavoriteStations(int userId)
    {
        return this.userRepository.listUserFavoriteStations(userId);
    }

    @Override
    public UserEntity login(UserEntity loginPassword) throws UnknownEmailException, WrongPasswordException
    {
        //check if the user email exists
        if (this.existsByEmail(loginPassword.getEmail()))
        {
            UserEntity user = this.findByEmail(loginPassword.getEmail());
            // if the entered password does not match with the user's password
            if(!this.pwHasher.matches(loginPassword.getPassword(), user.getPassword()))
            {
                throw new WrongPasswordException("The entered password is wrong", UserService.class);
            }
            // if the entered password match with the user's password
            else
            {
                return user;
            }
        }
        // if the user email does not exists
        else
        {
            throw new UnknownEmailException("Email " + loginPassword + " is unknown.", UserService.class, loginPassword.getEmail());
        }
    }


    @Override
    public void createAccount(UserEntity namesLoginPassword) throws NamesMissingException, InvalidVariablesLengthException, UnvalidEmailException, AccountAlreadyExistsException, TokenGenerationException
    {
        // when no names are given
        if(namesLoginPassword.getName().isEmpty() || namesLoginPassword.getFirstname().isEmpty())
        {
            throw new NamesMissingException("Name and First name were not specified", UserService.class);
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
            throw new InvalidVariablesLengthException("One or many params do not have the required length.", UserService.class, fieldsLength);
        }

        String userEmail = namesLoginPassword.getEmail();
        // Is email correctly formed ?
        EmailValidator validator = EmailValidator.getInstance();
        if(!validator.isValid(userEmail))
        {
            throw new UnvalidEmailException("Email " + userEmail + " is invalid.", UserService.class, userEmail);
        }

        // If it is valid, then we can check if the user already has an account or not.
        // If he already has one, server returns a 409 conflict error code with a message.
        // Checking if email is already used
        if(this.existsByEmail(userEmail))
        {
            throw new AccountAlreadyExistsException("Email already login.", UserService.class, namesLoginPassword.getEmail());
        }

        // if email is valid + account does not already login, we can begin the account creation process.

        // generating a unique api token for our user
        namesLoginPassword.setApiKey(TokenGenerator.generateUserApiToken());
        int cpt = 0;
        while(this.existsByApiKey(namesLoginPassword.getApiKey()))
        {
            namesLoginPassword.setApiKey(TokenGenerator.generateUserApiToken());
            cpt++;
            if(cpt > 100)
            {
                throw new TokenGenerationException("Could not find a unique token on server side... Token size has to be increased.", UserService.class);
            }
        }
        // hashing the user pw
        namesLoginPassword.setPassword(this.pwHasher.encode(namesLoginPassword.getPassword()));

        this.userRepository.save(namesLoginPassword);
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


}
