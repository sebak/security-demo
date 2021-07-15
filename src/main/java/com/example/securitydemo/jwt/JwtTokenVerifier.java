package com.example.securitydemo.jwt;

import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.securitydemo.jwt.AuthorisationConstant.*;

/**
 * OncePerRequestFilter because we want the filter be execute one per request from client
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtTokenVerifier extends OncePerRequestFilter {

    final JwtConfigProperties jwtConfigProperties;
    final SecretKey secretKey; // bean define in JwtSecretKey

    // we are not Autowiring it because we are going to pass it ourself from ApplicationSecurityConfig
    public JwtTokenVerifier(JwtConfigProperties jwtConfigProperties, SecretKey secretKey) {
        this.jwtConfigProperties = jwtConfigProperties;
        this.secretKey = secretKey;
    }

    /**
     * JwtTokenVerifier is a filter (step_10_jwt_request_filters.png) to check if the token is valid or not when the client sent it or use it on api
     * NB: We need to register this filter in ApplicationSecurityConfig in configure method
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // we get a token send by the client in header
        String authorizationHeader = request.getHeader(jwtConfigProperties.getAuthorizationHeader());

        if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(jwtConfigProperties.getTokenPrefix())) {
            // we reject the request in this case
            filterChain.doFilter(request, response);
            return;
        }

        // we get token and remove AUTHORIZATION_PREFIX_VALUE_HEADER_FOR_JWT value on it by replacing it with empty string
        String token = authorizationHeader.replace(jwtConfigProperties.getTokenPrefix(), "");
        try {
            /*
            Let say this is our token (go in jwt site to decode it and see what we are doing under)
            Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJsaW5kYSIsImF1dGhvcml0aWVzIjpbeyJhdXRob3JpdHkiOiJzdHVkZW50OndyaXRlIn0seyJhdXRob3JpdHkiOiJzdHVkZW50OnJlYWQifSx7ImF1dGhvcml0eSI6ImNvdXJzZTpyZWFkIn0seyJhdXRob3JpdHkiOiJST0xFX0FETUlOIn0seyJhdXRob3JpdHkiOiJjb3Vyc2U6d3JpdGUifV0sImlhdCI6MTYyNTA4MTcxOSwiZXhwIjoxNjI2MjEzNjAwfQ.cntj4ouB40tn6_WNjsH9BsfiyZDvJOCi9ePbVATjimu0xg8-gmkpDSC-OIK3fxpF8z1mWqsQhtu_uv-qDMKtNw

           when we decode the token we have in payload part:
           {
              "sub": "linda",
              "authorities": [
                {
                  "authority": "student:write"
                },
                {
                  "authority": "student:read"
                },
                {
                  "authority": "course:read"
                },
                {
                  "authority": "ROLE_ADMIN"
                },
                {
                  "authority": "course:write"
                }
              ],
              "iat": 1625081719,
              "exp": 1626213600
        }

            in this section we are going to get the values of the keys added in successfulAuthentication function in JwtUsernamePasswordAuthenticationFilter class
             */
            Jws<Claims> claimsJws = Jwts.parser()
                    // in successfulAuthentication we encode now we decode
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            // we get the body of token
            Claims body = claimsJws.getBody();
            String username = body.getSubject(); // the username is represent by subject in body when we decode token
            /*
            var type have been introduce in java 10 only for local variable and each time we use it we must directly assign a value to it or it will not compile
             */
            var authorities = (List<Map<String, String>> )body.get(AUTHORITIES_KEY);
            Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
                    .map(m -> new SimpleGrantedAuthority(m.get("authority"))) // look token decoded we are getting key authority from list of Map
                    .collect(Collectors.toSet());
            // we are getting authentication for user
            Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, simpleGrantedAuthorities);

            SecurityContextHolder.getContext().setAuthentication(authentication); // we are setting authentication to be true so the client that send the token is now authenticate
        } catch (JwtException e) {
            // the token have been modified or expired
            throw new IllegalStateException(String.format("Token %s can't not be trusted", token));
        }
        /* step_10_filter_pass_request_and_response_still_api.png so we must return the request and response of data to api by passing them to the next filter on filterChain still api
        if we don't do that we will not get the json response on api in our end point example http://localhost:7902/management/api/v1/student
         */
        filterChain.doFilter(request, response);
    }
}
