package com.polytech.mtonairserver.service.implementation;

import com.polytech.mtonairserver.customexceptions.accountcreation.*;
import com.polytech.mtonairserver.customexceptions.loginexception.*;
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
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Service implementation for the user service.
 */
@Service
public class UserService implements IUserService
{
    private UserRepository userRepository;

    private final int encryptionStrength = 10;

    private BCryptPasswordEncoder pwHasher = new BCryptPasswordEncoder(encryptionStrength, new SecureRandom());

    @Autowired
    public UserService(UserRepository ur)
    {
        this.userRepository = ur;
    }

    /**
     * Gets all the users, without their favoriteStations.
     * @return all the users from the mtonair database.
     */
    @Override
    public List<UserEntity> findAllUsersWithoutTheirFavoriteStations()
    {
        List<UserEntity> res = this.userRepository.findAll();
        res.forEach( sta -> sta.getUserFavoriteStationsByIdUser().clear());
        return res;
    }

    @Override
    public UserEntity findByIdUser(int id)
    {
        return this.userRepository.findById(id).get();
    }

    @Override
    public boolean existsByEmail(String email)
    {
        return this.userRepository.existsByEmail(email);
    }

    @Override
    public UserEntity findByEmail(String email)
    {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean existsByApiKey(String apiKey)
    {
        return this.userRepository.existsByApiKey(apiKey);
    }

    @Override
    public Set<StationEntity> listUserFavoriteStations(int userId)
    {
        UserEntity ue = this.findByIdUser(userId);
        return ue.getUserFavoriteStationsByIdUser();
    }

    /**
     * Logs in the user with his e-mail + password contained within an UserEntity
     * @param loginPassword The other fields of the UserEntity may be null/empty.
     * @return the full UserEntity that corresponds to the given email / password.
     */
    @Override
    public UserEntity login(UserEntity loginPassword) throws UnknownEmailException, WrongPasswordException
    {
        if (this.existsByEmail(loginPassword.getEmail()))
        {
            UserEntity userFromDatabase = this.findByEmail(loginPassword.getEmail());
            if(!this.pwHasher.matches(loginPassword.getPassword(), userFromDatabase.getPassword()))
            {
                throw new WrongPasswordException("The entered password is wrong", UserService.class);
            }
            else
            {
                return userFromDatabase;
            }
        }
        else
        {
            throw new UnknownEmailException("Email " + loginPassword.getEmail() + " is unknown.", UserService.class, loginPassword.getEmail());
        }
    }


    /**
     * Creates a new accout with a given UserEntity.
     * @param namesLoginPassword a UserEntity that only contains an email, a firstName+surName and a password.
     */
    @Override
    public void createAccount(UserEntity namesLoginPassword) throws NamesMissingException, InvalidVariablesLengthException, InvalidEmailException, AccountAlreadyExistsException, TokenGenerationException
    {
        this.checkIfUserEntityNamesArePresent(namesLoginPassword);
        this.checkIfUserEntityFieldsLenghtAreValid(namesLoginPassword);
        this.checkIfEmailIsValid(namesLoginPassword);
        if(this.existsByEmail(namesLoginPassword.getEmail()))
        {
            throw new AccountAlreadyExistsException("Email is already registered.", UserService.class, namesLoginPassword.getEmail());
        }

        // if the params are valid + account does not already exists, we can begin the account creation process.
        this.initializeApiTokenForNewUser(namesLoginPassword);

        namesLoginPassword.setPassword(this.pwHasher.encode(namesLoginPassword.getPassword()));
        this.userRepository.save(namesLoginPassword);
    }

    private void checkIfUserEntityNamesArePresent(UserEntity userEntityToTest) throws NamesMissingException
    {
        // when no names are given
        if(userEntityToTest.getName().isEmpty() || userEntityToTest.getFirstname().isEmpty())
        {
            throw new NamesMissingException("Name and First name were not specified", UserService.class);
        }
    }

    private void checkIfUserEntityFieldsLenghtAreValid(UserEntity userEntityToTest) throws InvalidVariablesLengthException
    {
        //pw (non hashed): between 6 and 32 chars
        //names between 1 to 50 chars
        //email : up to 75chars
        boolean oneParamInvalid = false;
        HashMap<String, Integer> fieldsLength = new HashMap<>();
        if(! this.checkVariableLength(6, 32, userEntityToTest.getPassword()))
        {
            oneParamInvalid = true;
            fieldsLength.put("Password", userEntityToTest.getPassword().length());
        }
        if(! this.checkVariableLength(1, 50, userEntityToTest.getName())
                || ! this.checkVariableLength(1, 50, userEntityToTest.getFirstname()))
        {
            oneParamInvalid = true;
            fieldsLength.put("Name", userEntityToTest.getName().length());
            fieldsLength.put("First name", userEntityToTest.getFirstname().length());

        }
        if(! this.checkVariableLength(0, 75, userEntityToTest.getEmail()))
        {
            oneParamInvalid = true;
            fieldsLength.put("E-mail", userEntityToTest.getEmail().length());
        }

        if(oneParamInvalid)
        {
            throw new InvalidVariablesLengthException("One or many params do not have the required length.", UserService.class, fieldsLength);
        }
    }

    private void checkIfEmailIsValid(UserEntity userEntityToTest) throws InvalidEmailException
    {
        String userEmail = userEntityToTest.getEmail();
        // Is email correctly formed ?
        EmailValidator validator = EmailValidator.getInstance();
        if(!validator.isValid(userEmail))
        {
            throw new InvalidEmailException("Email " + userEmail + " is invalid.", UserService.class, userEmail);
        }
    }

    private void initializeApiTokenForNewUser(UserEntity userWithoutApiToken) throws TokenGenerationException
    {
        userWithoutApiToken.setApiKey(TokenGenerator.generateUserApiToken());
        int cpt = 0;
        while(this.existsByApiKey(userWithoutApiToken.getApiKey()))
        {
            userWithoutApiToken.setApiKey(TokenGenerator.generateUserApiToken());
            cpt++;
            if(cpt > 100)
            {
                throw new TokenGenerationException("Could not find a unique token on server side... Token size has to be increased.", UserService.class);
            }
        }
    }

    /**
     * Private helper that helps to know whether or not a given string has min to max characters.
     * @return true if str has min (included) to max (included) chars. False otherwise.
     */
    private boolean checkVariableLength(int min, int max, String str)
    {
        return (str.length() >= min && str.length() <= max);
    }
}
