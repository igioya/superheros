package com.superheroes.services;

import com.superheroes.config.security.JwtUtils;
import com.superheroes.exceptions.persistence.NotFoundException;
import com.superheroes.model.User;
import com.superheroes.webservices.reponse.CredentialsResponse;
import com.superheroes.webservices.request.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserService userService;

    public CredentialsResponse getJwt(AuthRequest authRequest) throws NotFoundException {
        User user = this.userService.getUser(authRequest.getUsername());
        if (!user.getPassword().equals(authRequest.getPassword())) {
            throw new NotFoundException();
        }
        String jwt = JwtUtils.generateToken(authRequest.getUsername());
        CredentialsResponse credentialsResponse = new CredentialsResponse();
        credentialsResponse.setToken(jwt);
        credentialsResponse.setPrefix("Bearer");
        return credentialsResponse;
    }
}
