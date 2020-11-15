package com.polytech.mtonairserver.service.implementation;

import com.polytech.mtonairserver.model.entities.UserEntity;
import com.polytech.mtonairserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public JwtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = null;
        userEntity = this.userRepository.findByEmail(email);

        if (userEntity != null) {
            return new User(userEntity.getEmail(), userEntity.getPassword(), new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found with email : " + email);
        }
    }
}
