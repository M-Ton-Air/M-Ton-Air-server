package com.polytech.mtonairserver.service.interfaces;

import com.polytech.mtonairserver.customexceptions.accountcreation.*;
import com.polytech.mtonairserver.customexceptions.loginexception.UnknownEmailException;
import com.polytech.mtonairserver.customexceptions.loginexception.WrongPasswordException;
import com.polytech.mtonairserver.customexceptions.favoritestation.StationAlreadyInUserFavoriteStationsException;
import com.polytech.mtonairserver.model.entities.StationEntity;
import com.polytech.mtonairserver.model.entities.UserEntity;

import java.util.List;
import java.util.Set;

public interface IUserService
{
    List<UserEntity> findAllUsersWithoutTheirFavoriteStations();

    UserEntity findByIdUser(int id);

    boolean existsByEmail(String email);

    UserEntity findByEmail(String email);

    boolean existsByApiKey(String apiKey);

    void createAccount(UserEntity user) throws NamesMissingException, InvalidVariablesLengthException, InvalidEmailException, AccountAlreadyExistsException, TokenGenerationException;

    UserEntity login(UserEntity loginPassword) throws UnknownEmailException, WrongPasswordException;

}
