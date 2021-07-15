package com.example.securitydemo.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static com.example.securitydemo.jwt.AuthorisationConstant.*;

// implement step_10_jwt_how_it_work.png
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    // to see if authentication are correct we use AuthenticationManager
    final AuthenticationManager authenticationManager;
    final JwtConfigProperties jwtConfigProperties;
    final SecretKey secretKey; // bean define in JwtSecretKey

    // we are not Autowiring it because we are going to pass it ourself from ApplicationSecurityConfig
    public JwtUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager, JwtConfigProperties jwtConfigProperties, SecretKey secretKey) {
        this.authenticationManager = authenticationManager;
        this.jwtConfigProperties = jwtConfigProperties;
        this.secretKey = secretKey;
    }

    /**
     * get the username and password send by the client and validate step one of step_10_jwt_how_it_work.png
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        /**
         * we read username and password from request and put it in UsernamePasswordAuthenticationRequest object
         */
        try {
            UsernamePasswordAuthenticationRequest usernamePasswordAuthenticationRequest = new ObjectMapper().readValue(request.getInputStream(), UsernamePasswordAuthenticationRequest.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(usernamePasswordAuthenticationRequest.getUsername(), usernamePasswordAuthenticationRequest.getPassword());
            //ma sure that user exist if so verify if password is correct
            return authenticationManager.authenticate(authentication);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * generate token  and send it to client step 2 of step_10_jwt_how_it_work.png
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        String token = Jwts.builder()
                /**
                 * got to https://jwt.io/ Subject correspond to header, claim to payload, signWith to signature
                 */
                .setSubject(authResult.getName())
                .claim(AUTHORITIES_KEY, authResult.getAuthorities()) // value is the list of authorities of the user
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfigProperties.getTokenExpirationAfterDays()))) // sql Date here
                .signWith(secretKey)
                .compact();

                response.addHeader(jwtConfigProperties.getAuthorizationHeader(), jwtConfigProperties.getTokenPrefix() + token) ; // add token to response header with Bearer plus space concatenation instead of Basic
    }
}
