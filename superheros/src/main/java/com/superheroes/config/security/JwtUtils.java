package com.superheroes.config.security;

import io.jsonwebtoken.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static java.util.Collections.emptyList;

public class JwtUtils {

    public static String generateToken(String username) {
        String token = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + 600000000))
                .signWith(SignatureAlgorithm.HS512, "SECRET_KEY")
                .compact();


        return token;
    }

    public static Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        try{
            if (token != null) {
                String user = Jwts.parser()
                        .setSigningKey("SECRET_KEY")
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject();
                return user != null ?
                        new UsernamePasswordAuthenticationToken(user, null,  emptyList()) :
                        null;
            }
        } catch(ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException exception){
            return null;
        }
        return null;
    }
}
