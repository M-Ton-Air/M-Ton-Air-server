package com.polytech.mtonairserver.service.interfaces;

import com.polytech.mtonairserver.customexceptions.accountcreation.*;
import com.polytech.mtonairserver.customexceptions.loginexception.UnknownEmailException;
import com.polytech.mtonairserver.customexceptions.loginexception.WrongPasswordException;
import com.polytech.mtonairserver.model.entities.StationEntity;
import com.polytech.mtonairserver.model.entities.UserEntity;
import com.polytech.mtonairserver.repository.UserRepository;

import java.util.Collection;
import java.util.List;

public interface IUserService
{
    List<UserEntity> findAll();

    UserEntity findByIdUser(int id);

    boolean existsByEmail(String email);

    UserEntity findByEmail(String email);

    boolean existsByApiKey(String apiKey);

    void createAccount(UserEntity user) throws NamesMissingException, InvalidVariablesLengthException, UnvalidEmailException, AccountAlreadyExistsException, TokenGenerationException;

    public UserEntity login(UserEntity loginPassword) throws UnknownEmailException, WrongPasswordException;

    public Collection<StationEntity> listUserFavoriteStations(int userId);
}
