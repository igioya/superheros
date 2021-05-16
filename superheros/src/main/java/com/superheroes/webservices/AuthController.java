package com.superheroes.webservices;

import com.superheroes.exceptions.persistence.NotFoundException;
import com.superheroes.services.AuthService;
import com.superheroes.utils.Log;
import com.superheroes.webservices.reponse.CredentialsResponse;
import com.superheroes.webservices.reponse.ErrorResponse;
import com.superheroes.webservices.reponse.SuccessResponse;
import com.superheroes.webservices.request.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> auth(@RequestBody AuthRequest authRequest) {
        try {
            CredentialsResponse credential = this.authService.getJwt(authRequest);
            SuccessResponse successResponse = new SuccessResponse();
            successResponse.setData(credential);
            return new ResponseEntity<>(JsonHelper.fromObjectToJSON(successResponse), HttpStatus.OK);
        } catch (NotFoundException e) {
            Log.debug(e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setData(e.getMessage());
            return new ResponseEntity<>(JsonHelper.fromObjectToJSON(errorResponse), HttpStatus.UNAUTHORIZED);
        }
    }
}

