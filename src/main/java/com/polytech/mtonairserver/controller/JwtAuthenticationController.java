package com.polytech.mtonairserver.controller;


import com.polytech.mtonairserver.config.JwtTokenUtil;
import com.polytech.mtonairserver.model.entities.UserEntity;
import com.polytech.mtonairserver.model.security.JwtResponse;
import com.polytech.mtonairserver.repository.UserRepository;
import com.polytech.mtonairserver.service.implementation.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

// https://www.javainuse.com/spring/boot-jwt

@RequestMapping("/authentification")
@RestController
@CrossOrigin
public class JwtAuthenticationController {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    private UserRepository userRepository;

    // on initialise
    @Autowired
    public JwtAuthenticationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // auhentification  qui va généré un jeton
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody UserEntity userEntity) throws Exception {
        // On contrôle l'utilisateur
        authenticate(userEntity.getEmail(), userEntity.getPassword());
        try {
            // on récupère les informations
            // nouvel accès à la base de données++6
            final UserDetails userDetails = userDetailsService.loadUserByUsername(userEntity.getEmail());
            // On génère le jeton
            final String token = jwtTokenUtil.generateToken(userDetails);
            // on retourne le jeton dans un flux json
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
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

}
